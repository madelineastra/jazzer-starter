# jazzer-starter
Just a simple script designed to help expedite fuzzing

    mvn package
    /path/to/jazzer --instrumentation-includes=java.io.** --cp=target/jazzer-starter-1.0-SNAPSHOT-jar-with-dependencies.jar --target_class=FuzzDriver inputs
