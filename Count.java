
import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.MyKmeans.MapReduce.Map;
import org.apache.hadoop.MyKmeans.MapReduce.Reduce;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.examples.WordCount;
import org.apache.hadoop.examples.WordCount.IntSumReducer;
import org.apache.hadoop.examples.WordCount.TokenizerMapper;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class Count {
	public static class TokenizerMapper 
    extends Mapper<Object, Text, Text, IntWritable>{

		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		public void map(Object key, Text value, Context context
				) throws IOException, InterruptedException {
			String line = value.toString();
			StringTokenizer tokens = new StringTokenizer(line,"\n");
			while(tokens.hasMoreTokens()){
				String tmp = tokens.nextToken();
				StringTokenizer sz = new StringTokenizer(tmp);
				String name = sz.nextToken();
				Text outName = new Text(name);
				context.write(outName, one);
		}
		}
}

	public static class IntSumReducer 
    	extends Reducer<Text,IntWritable,Text,IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values, 
                    	Context context
					) throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			context.write(key, result);
		}
}
	public static void run(String readPath,String countPath) throws IOException, ClassNotFoundException, InterruptedException{
         
	    Configuration conf = new Configuration();
		
		Job job = new Job(conf, "count");
		job.setJarByClass(Count.class);
		job.setMapperClass(TokenizerMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);


		FileInputFormat.addInputPath(job, new Path(readPath));
		FileOutputFormat.setOutputPath(job, new Path(countPath));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
     }
	
}
