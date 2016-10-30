import java.io.*;
public class HelloWorld {
    static int diffb (int bearing, int heading) {
      return ((((bearing - heading) % 360) + 540) % 360) - 180;

    }
    public static void main(String[] args) {
        System.out.println("Hello, World");
        HelloJens jens = new HelloJens();
	Smooth smooth = new Smooth(5);

	System.out.println(String.format("%d",diffb(245,266)));
	System.out.println(String.format("%d",diffb(350,10)));
	System.out.println(String.format("%d",diffb(10,350)));
	System.out.println(String.format("%d",diffb(180,0)));

	String fname = "data";
	String line = null;

	try {
          FileReader fileReader = new FileReader(fname);
          BufferedReader bufferedReader = new BufferedReader(fileReader);
  
          while((line = bufferedReader.readLine()) != null) {
            System.out.println("Average: "+smooth.avg(line)+" Value: "+line);
  	  }   
  	  bufferedReader.close();
	} catch(FileNotFoundException ex) {
           System.out.println( "Unable to open file '" + fname + "'");                
        } catch(IOException ex) {
           System.out.println( "Error reading file '" + fname + "'");                  
        }
    }

}

