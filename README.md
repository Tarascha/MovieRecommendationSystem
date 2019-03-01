# MovieRecommendationSystem  
  
 This is the Data Processing part of a Movie Recommendation System based on an item-based collaborative filtering algorithm  
 
 This program should run in Hadoop environment   
 Procedure to run this program:  
 1.Download Docker and make sure docker runs on your computer;  
 2.Set up Hadoop Cluster in Docker:  
    mkdir bigdata-class2  
    cd bigdata-class2   #create and step into a folder  
    sudo docker pull joway/hadoop-cluster   #pull mirror image from dockerhub  
    git clone https://github.com/joway/hadoop-cluster-docker   
    sudo docker network create --driver=bridge hadoop   
    cd hadoop-cluster-docker   
    sudo ./start-container.sh  #step into container  
    ./start-hadoop.sh  #start hadoop  
    cd src #step into src folder  
    
  3.Put this program into src folder, then do as follow in command line:  
  
      cd RecommenderSystem 

      hdfs dfs -mkdir /input  

      hdfs dfs -put input/* /input  # put the input data in "input" folder into HDFS

      hdfs dfs -rm -r /dataDividedByUser  

      hdfs dfs -rm -r /coOccurrenceMatrix  

      hdfs dfs -rm -r /Normalize  

      hdfs dfs -rm -r /Multiplication  

      hdfs dfs -rm -r /Sum  

      cd src/main/java/  
 
      hadoop com.sun.tools.javac.Main *.java  

      jar cf recommender.jar *.class  

      hadoop jar recommender.jar Driver /input /dataDividedByUser /coOccurrenceMatrix /Normalize /Multiplication /Sum  

      #args0: original dataset  

      #args1: output directory for DividerByUser job  

      #args2: output directory for coOccurrenceMatrixBuilder job  

      #args3: output directory for Normalize job  

      #args4: output directory for Multiplication job  

      #args5: output directory for Sum job  
      
If all things go well, then the processed data is stored in "Sum" folder in HDFS, you can view it by typing following in command line:  
    hdfs dfs -cat /Sum/*  
