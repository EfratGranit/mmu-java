import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.hit.dm.DataModel;

/*
 * 
 * THIS CLASS WILL INITIALIZE MY 'RAM'
 * FOR STARTING THE PROJECT WE WERE GUIDED TO INITIALIZE OUR
 * RAM TO 1000 NULL PAGES
 * MEANING WE HAVE 1000 ID'S (1-1000) WITH NULL VALUE (CONTENT)
 * 
 * RUN THIS MAIN TO INITIALIZE THE RAM
 * 
 */

public class createMyFile {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		DataModel<String> dm = null;
		Long id;
		String content="null";
		String file_path = new String("src/main/resources/datasource.txt");
		
		File my_data_source_file = new File(file_path);
		
		//only if file does not exist (first time running of project) - create and initialize it
		if (!my_data_source_file.exists())
		{
			System.out.println("First time. creating RAM!");
			
			my_data_source_file.createNewFile();
			
			//writing part
			FileOutputStream fos = new FileOutputStream(file_path);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			for (long i=1; i<=1000;i++)
			{
				id = (Long) i;
				dm = new DataModel<String>(id,content);
				os.writeObject(dm);
				os.flush();
			}
			
			os.close();
		} else {
			//TODO remove the reading part
			FileInputStream fis = new FileInputStream(file_path);
			ObjectInputStream is = new ObjectInputStream(fis);
			if (fis.getChannel().size() != 0)
			{
				while (fis.available()>0)
				{
					dm = (DataModel<String>)is.readObject();
					System.out.println(dm);
				}
			}
			is.close();
			fis.close();
		}
	}
}
