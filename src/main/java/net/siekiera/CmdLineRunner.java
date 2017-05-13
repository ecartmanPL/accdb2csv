package net.siekiera;

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Eric on 13.05.2017.
 */
@Component
public class CmdLineRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(CmdLineRunner.class);

    @Override
    public void run(String... strings) throws Exception {
        Options options = parseCommandLineOptions(strings);
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try {
            cmd = parser.parse(options, strings);
        } catch (Exception e) {
            logger.error("Exception caught! " + e.getMessage());
            return;
        }
        File inputFile = null;
        Database accessDb = null;
        String tableName = null;
        Table accessTable = null;
        FileWriter writer = null;

        if (cmd.hasOption("db")) {
            inputFile = new File(cmd.getOptionValue("db"));
            accessDb = DatabaseBuilder.open(inputFile);
        }

        if (cmd.hasOption("table")) {
            tableName = cmd.getOptionValue("table");
            accessTable = accessDb.getTable(tableName);
        }

        if (cmd.hasOption("out")) {
            writer = new FileWriter(cmd.getOptionValue("out"));
        }

        if (cmd.hasOption("db") && cmd.hasOption("out")&& cmd.hasOption("table")) {
            writeCsv(accessTable, writer);
        } else if (cmd.hasOption("db")&& cmd.hasOption("show")) {
            showTables(accessDb);
        } else {
            showHelp(options);
        }
    }

    private void showHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.setSyntaxPrefix("Usage: ");
        formatter.printHelp("java -jar accdb2csv.jar [options]", "Avaliable options:", options, "");
    }

    private void writeCsv(Table accessTable, FileWriter writer) throws IOException {
        for (Row row : accessTable) {
            String csvLine = CSVUtility.convertObjectsToCsvLine(row);
            writer.append(csvLine);
        }
        writer.flush();
        writer.close();
    }

    private void showTables(Database db) throws IOException {
        logger.info("Showing tables.");
        System.out.println("Table names:");
        for (String tableName : db.getTableNames()) {
            System.out.println(tableName);
        }
    }

    private Options parseCommandLineOptions(String[] strings) {
        Options options = new Options();
        Option database = Option.builder("db")
                .argName("input.accdb")
                .hasArg()
                .desc("Database file (MS Access format).")
                .build();
        Option output = Option.builder("out")
                .argName("output.csv")
                .hasArg()
                .desc("CSV output file.")
                .build();
        Option table = Option.builder("table")
                .argName("tablename")
                .hasArg()
                .desc("DB table name to read.")
                .build();
        Option showTables = Option.builder("show")
                .desc("Just show tables and exit.")
                .build();
        Option help = Option.builder("help")
                .desc("Just show help and exit.")
                .build();

        options.addOption(database);
        options.addOption(output);
        options.addOption(table);
        options.addOption(showTables);
        options.addOption(help);

        return options;
    }
}
