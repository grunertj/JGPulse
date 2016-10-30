import java.io.*;
public class SmoothTest {

  public static void main(String[] args) {
    Smooth smooth = new Smooth(11);
    Smooth smootha = new Smooth(11);
    String fname = "data";
    String line = null;
    String[] array;

    try {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(fname));
      BufferedWriter bufferedWriterAvg = new BufferedWriter(new FileWriter("data.avg"));
      BufferedWriter bufferedWriterMax = new BufferedWriter(new FileWriter("data.max"));

      while((line = bufferedReader.readLine()) != null) {
        array = line.split("\\s+");
	//System.out.println("Hello, World " + array[0] + " " + String.format("%6f",smooth.avg(array[0])));
        // bufferedWriterAvg.append(String.format("%6f\n",5+smooth.avg(array[0])));
        // bufferedWriterMax.append(String.format("%6f %s\n",smooth.max(array[0]),smooth.is_max(array[0])));
        // bufferedWriterMax.append(String.format("Min %6f %s\n",smooth.min(array[0]),smooth.is_min(array[0])));
        bufferedWriterMax.append(String.format("Max %s %s %s\n",array[0],smooth.max(array[0]),smooth.is_max(array[0])));
        bufferedWriterMax.append(String.format("Min %s %s %s\n",array[0],smooth.min(array[0]),smooth.is_min(array[0])));
      }
      bufferedReader.close();
      bufferedWriterAvg.close();
      bufferedWriterMax.close();

    } catch(FileNotFoundException ex) {
      System.out.println( "Unable to open file '" + fname + "'");                
    } catch(IOException ex) {
      System.out.println( "Error reading file '" + fname + "'");                  
    }

  }
}

