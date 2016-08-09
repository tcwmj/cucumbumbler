#package
#make this part of the build later
mvn clean compile 
mvn clean compile assembly:single

#cucumbumbler
#java -cp target/classes/:target/* com.cucumbumbler.app.CucumbumblerMain "$@"