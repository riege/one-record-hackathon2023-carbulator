package com.riege.onerecord.carbulator;

import java.io.File;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

// Cmdlin parsing as per
// https://opensource.com/article/21/8/java-commons-cli
// and
// https://lightrun.com/java-tutorial-java-command-line-arguments/
public class CommandLine {

    private final Options options;
    private org.apache.commons.cli.CommandLine cmd = null;

    private final Option inputFiles;
    private final Option outputFile;
    private final Option apikey;
    private final Option carbonCareURLPrefix;
    private final Option carbonCareServerPort;

    public CommandLine() {
        options = new Options();
        apikey = Option.builder("a").longOpt("apikey")
            .argName("key")
            .hasArg()
            .required()
            .desc("CarbonCare API key").build();
        inputFiles = Option.builder("i").longOpt("input")
            .argName("fqfn")
            .hasArg()
            .required(false)
            .desc("input single JSON bookingOption (filename), multiple use possible").build();
        outputFile = Option.builder("o").longOpt("output")
            .argName("fqfn")
            .hasArg()
            .required(false)
            .desc("output file name for carbonated JSON array of BookingOptions")
            .build();
        carbonCareURLPrefix = Option.builder("h").longOpt("hosturlprefix")
            .argName("hostprefix")
            .hasArg()
            .required(false)
            .desc("URL Prefix of the CarbonCare server API URL")
            .build();
        carbonCareServerPort = Option.builder("p").longOpt("hostport")
            .argName("port")
            .hasArg()
            .required(false)
            .desc("Port for CarbonCare server API URL")
            .build();
        options.addOption(apikey);
        options.addOption(inputFiles);
        options.addOption(outputFile);
        options.addOption(carbonCareURLPrefix);
        options.addOption(carbonCareServerPort);
    }
    public CommandLine(String[] args){
        this();
        parse(args);
    }

    public void parse(String[] args) {
        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            formatter.printHelp("Command line options:", options);
            System.exit(1);
            return;
        }
    }

    private void checkParsed() {
        if (cmd == null) {
            throw new IllegalStateException("Commandline had not been parsed properly yet");
        }
    }

    public String[] getInputSources() {
        checkParsed();
        String[] inputSources = cmd.getOptionValues(inputFiles);
        if (inputSources == null) {
            // inputSources = new String[] { "/var/tmp/input.json" };
            inputSources = new String[] {
                "FRA-ORD-LH-001.json",
                "FRA-ORD-LH-002.json",
                "FRA-ORD-LH-003.json",
                "FRA-ORD-LX-001.json",
            };
            inputSources = new String[] {
                "HACKATHON-001.json",
                "HACKATHON-002.json",
                "HACKATHON-003.json",
                "HACKATHON-004.json",
                "HACKATHON-005.json",
            };
        }
        return inputSources;
    }

    public String getOutputFile() {
        checkParsed();
        String output = cmd.getOptionValue(outputFile);
        if (output == null) {
            try {
                return File.createTempFile("output", ".json").getAbsolutePath();
            } catch (Exception e) {
                return "/var/tmp/output.json";
            }
        }
        return output;
    }

    public String getApiKey() {
        checkParsed();
        String result = cmd.getOptionValue(apikey);
        if (result == null) {
            // return "k3yf0rd3m0c4rb0nc4r3";
            return "ecfb323f-0df5-efb2-1d0b-fad90aa4d1ad";
        }
        return result;
    }

    public String getCarbonCareURLPrefix() {
        checkParsed();
        String result = cmd.getOptionValue(carbonCareURLPrefix);
        if (result == null) {
            return "https://192.168.1.106";
        }
        return result;
    }

    public int getCarbonCareServerPort() {
        checkParsed();
        String result = cmd.getOptionValue(carbonCareServerPort);
        if (result == null) {
            return 814;
        }
        return Integer.getInteger(result);
    }

}
