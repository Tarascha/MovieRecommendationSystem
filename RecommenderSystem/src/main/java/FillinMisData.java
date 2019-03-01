

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import javax.security.auth.login.AppConfigurationEntry;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FillinMisData {

    public static class FillinDataMapper extends Mapper<LongWritable, Text, IntWritable, Text>{

        //map method
        @Override
        public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
            //input user,movie, rating
            String[] user_movie_rating = value.toString().trim().split(",");
            int userID = Integer.parseInt(user_movie_rating[0]);
            String movieID = user_movie_rating[1];
            String rating = user_movie_rating[2];

            context.write(new IntWritable(userID), new Text(movieID + ":" + rating));
        }
    }

    public static class FillinDataReducer extends Reducer<IntWritable, Text, IntWritable, Text>{

        //reduce method
        @Override
        public void reduce(IntWritable key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
            //key = userID, <movie1:rating, moive2:rating......>
            //outputkey : uesrID, outputvalue movie,rating

            Map<Integer, Integer> map = new HashMap<Integer, Integer>();
            double ratesum = 0;
            int totalcount = 0;
            while(values.iterator().hasNext()){
                String[] cur = values.iterator().next().toString().trim().split(":");
                int movieId = Integer.parseInt(cur[0]);
                double rating = Double.valueOf(cur[1]);
                map.put(movieId, 1);
                ratesum += rating;
                totalcount++;
                context.write(key, new Text(movieId + "," + rating));
            }
            double avgrate = ratesum / totalcount;

            for(int i = 10001; i <= 10006; i++){
                if(!map.containsKey(i)){
                    context.write(key, new Text(i + "," + avgrate));
                }
            }

        }
    }

    public static void main(String[] args) throws Exception{

        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);
        job.setMapperClass(FillinDataMapper.class);
        job.setReducerClass(FillinDataReducer.class);

        job.setJarByClass(FillinMisData.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        job.setOutputKeyClass(IntWritable.class);
        job.setOutputValueClass(Text.class);

        TextInputFormat.setInputPaths(job, new Path(args[0]));
        TextOutputFormat.setOutputPath(job, new Path(args[1]));

        job.waitForCompletion(true);




    }

}
