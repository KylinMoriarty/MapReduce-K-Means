
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;


import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.io.IOUtils;
import org.apache.log4j.Logger;


public class Write {
	
public static void main(String[] args) throws IOException
{
	Configuration conf = new Configuration();
	Path inFile = new Path("/user/kylin/input/centers.txt");
        //Path inFile = new Path("/user/kylin/input/wine.txt");
	FileSystem hdfs = FileSystem.get(conf);
	FSDataOutputStream os = hdfs.create(inFile);
	
	
	String localFile ="/home/kylin/Desktop/test2.txt";
        //String localFile ="/home/kylin/Desktop/test.txt";
	InputStream in=new BufferedInputStream(new FileInputStream(localFile));  
    OutputStream out=hdfs.create(inFile);  
    IOUtils.copyBytes(in, out, conf);  
	
	//os.writeUTF("Chinese Hadoop Community\n");
	//os.flush();
	os.close();
}
}
