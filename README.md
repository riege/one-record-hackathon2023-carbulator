## one-record-hackathon2023-carbulator

# The Carbulator

For more background information, see devpost https://devpost.com/software/the-carbulator.

This project has been created during 
_IATA ONE Record Hackathon – hosted by Lufthansa Cargo, 23-25 June 2023_, see https://onerecord-fra.devpost.com/

## Toolbox
This project uses public available tools / libraries, mainly

* https://github.com/riege/one-record-ontologydatamodel (UPL-1.0 license) library containing Java POJO classes for ONE Record DataModel.
* https://github.com/riege/one-record-jsonutils (UPL-1.0 license) library to write and read ONE Record JSON files.
* https://my.carboncare.ch/ISO-API_3.0.zip CarbonCare CO₂ calculation API
* More open source Java utility APIs to be used (e.g. Apache Commandline Scanner API etc).
  The project is build with open source CI tools (maven or gradle) and all required API library dependencies listed in the project configuration file engine/build.gradle.kts.

# Developer Info

## Basic startup
Make sure the `repository` is checked out and everything starts from that directory.

Build the project (you need a Java compiler):

    ./gradlew shadowJar

Then start the engine with default parameter: 

    java -jar engine/build/libs/engine-all.jar

Best viewed in a 100x40 character terminal..

## Commandline Options
During the Hackathon event and development, the Carbulator Engine connected 
to a local CarbonCare API server copy, which was added to the local network.
This server was already using the new CarbonCare calculation API 3.0 (see link below) which will get 
productive officially on July 1st, 2023 and which uses the new 
CO2 calculation standard ISO-14083:2023.

For future production usage, the address of CarbonCare API server needs to be 
provided together with a APIKEY. Commandline options to the Carbulator Engine are:

### CarbonCare API key
-a,--apikey <key>                 

### URL Prefix of the CarbonCare server API URL
-h,--hosturlprefix <hostprefix>   

### Port for CarbonCare server API URL
-p,--hostport <port>              

### Input single JSON bookingOption (filename/URL), multiple use possible
-i,--input <fqfn>                 

### Output file name for carbonated JSON array of BookingOptions
-o,--output <fqfn>                


## Developer Source Code Info

Java class `DemoDataGenerator` generates ONE Record BookingOption .json files 
into `engine/src/main/resources`. 

Note that `CommandLine#getInputSources()` uses (some) of these files as default
input for the `CarbulatorEngine` class.

This approach (generating files + parsing them) has been choosen intentionally
to 
 * ensure JSON example files are correct as per Ontology
 * allow to feed other JSON files to `CarbulatorEngine` as well, as long as their content matches the BookingOption requirements and rules.
