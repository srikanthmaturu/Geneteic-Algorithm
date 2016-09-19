package javaapplication2;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author srikanth maturu
 */
public class GeneticAlgorithm {

 int RandomGenerationExperiment=0;   
 String DesiredString=new String("SEandHeuristic_AI");
 String Population[];
 int SizeofPopulation=16;
 int FitnessOfPopulation[];
 static int FitnessType=1;
 int NoOfGenerations=0;
 int LowerBound=65;
 int UpperBound=122;
 static double MutationRate=0.03;
 static BufferedImage DisplayMatrix=new BufferedImage(2000,20000,TYPE_INT_RGB );;
 BufferedImage ReducedDisplayMatrix;
 int HighestFitness=0;
 
 static int visualize=0;
   
 public static void main(String[] args) throws IOException {
     GeneticAlgorithm ga=new GeneticAlgorithm();
     System.out.println("Enter the Fitness Function type 1 or 2 :");
       Scanner s= new Scanner(System.in);
       FitnessType=s.nextInt();
     System.out.println("Enter the Mutation rate 0.01,0.02,0.03,0.04,0.05 or so on:");
     MutationRate=s.nextDouble();
     System.out.println("Enter if a visulaization is needed: Not Needed=0 and Needed =1");
     visualize=s.nextInt();
     System.out.println("List of Settings :\n"+"Fitness Function :"+FitnessType+"\nMutation Rate :"+MutationRate+"\nVisulize:"+visualize);
 
     ga.runAlgorithm();
        
}
void crossOver()
{
Random rand=new Random();
String TempPopulation[]=new String[16];
for(int i=0;i<8;i++)
{
    TempPopulation[i]=Population[i];
}

for(int i=0,k=8;i<8;i++)
{
    char child1[]=new char[17];
    char child2[]=new char[17];
    int crossoverpoint=rand.nextInt(16)+1;
   for(int j=0;j<17;j++)
   {
       if(i<crossoverpoint)
       {
          child1[j]=Population[i].charAt(j);
          child2[j]=Population[i+2].charAt(j);
       }
       else
       {
        child1[j]=Population[i+2].charAt(j);
        child2[j]=Population[i].charAt(j); 
           
       }
   }
   TempPopulation[k]=String.valueOf(child1);
   k++;
   TempPopulation[k]=String.valueOf(child2);
   k++; 
   if(i==1)
   {
    i=3;
   }
   if(i==5)
   {
     i=7;  
   }
}
 Population=TempPopulation;
 SizeofPopulation=16;
  for(int i=0;i<SizeofPopulation;i++)
  {
      FitnessOfPopulation[i]=calculateFitness(FitnessType,Population[i]);
  }
    
}
void mutate()
{
    Random rand=new Random();
    boolean MutatedGenes[][]=new boolean[16][17];
  int NoOfGenes=(int)(17*16*MutationRate);
  for(int i=0;i<NoOfGenes;i++)
  {
    int chromosome=rand.nextInt(16);
    int gene=rand.nextInt(17);
    if(!MutatedGenes[chromosome][gene])
    {
        char element[]=Population[chromosome].toCharArray();
        element[gene]=(char)(LowerBound+rand.nextInt(58));
        Population[chromosome]=String.valueOf(element);
        MutatedGenes[chromosome][gene]=true;
    }
    else
    {
        i--;
    }   
  }
  for(int i=0;i<SizeofPopulation;i++)
  {
      FitnessOfPopulation[i]=calculateFitness(FitnessType,Population[i]);
  }
  
}
void selection()
{
 int LeastMemberIndex=100;
 int LeastMemberFitness=999999999;
 int SimilarPopulationIndexes[]=new int[20];
 int SimilarPopulationSize=0;
 boolean MemberSelection[]=new boolean[SizeofPopulation];
 String SelectedPopulation[]=new String[8];
 for(int i=0;i<8;i++)
 {
     SelectedPopulation[i]=new String();
     for(int k=0;k<SizeofPopulation;k++)
     {
      if(!MemberSelection[k])
      {
          if(LeastMemberFitness>FitnessOfPopulation[k])
          {
              if(LeastMemberFitness==FitnessOfPopulation[k])
              {
               SimilarPopulationIndexes[SimilarPopulationSize]=k;
               SimilarPopulationSize++;
              }
              else
              {
              SimilarPopulationSize=0;
              SimilarPopulationIndexes[SimilarPopulationSize]=k;
              SimilarPopulationSize++;
              LeastMemberFitness=FitnessOfPopulation[k];
              LeastMemberIndex=k;
              }
          }
      }
     }
     if(SimilarPopulationSize>1)
     {   int remaining=SimilarPopulationSize;
         boolean SelecedSimilarPopulation[]=new boolean[SimilarPopulationSize];
         Random rand=new Random();
         
      while(remaining!=0||i<8)
      {
         int l= rand.nextInt(remaining)+1;
          for(int j=0,count=1;j<SimilarPopulationSize;j++)
          {
              if(!SelecedSimilarPopulation[j])
              {
                  if(count==l)
                  {
                    SelecedSimilarPopulation[j]=true;
                    MemberSelection[SimilarPopulationIndexes[j]]=true;
                    SelectedPopulation[i]=Population[SimilarPopulationIndexes[j]];
                    i++;
                    remaining--;
                    break;
                  }
                  else
                  {
                  count++;
                  }
              }
          }
      }   
     }
     else
     {
     MemberSelection[LeastMemberIndex]=true;
     SelectedPopulation[i]=Population[LeastMemberIndex];
     }
     LeastMemberFitness=999999999;
}
 Population=SelectedPopulation;
 SizeofPopulation=8;
 
}
int calculateFitness(int type,String member)
{
    
    int Distance=0;
    if(type==1)
    {
    for(int i=0;i<17;i++)
    {
        Distance=Distance+((int)member.charAt(i)-(int)DesiredString.charAt(i))*((int)member.charAt(i)-(int)DesiredString.charAt(i));
    }
    }
    if(type==2)
    { 
    for(int i=0;i<17;i++)
    {
        if(DesiredString.charAt(i)!=member.charAt(i))
        {
            Distance++;
        }
    }   
    }
  return(Distance);  
}    
void initialPopulation()
{
  Random rand=new Random();
  Population=new String[32];
  FitnessOfPopulation=new int[32];
  SizeofPopulation=32;
  char Sample[]=new char[17];
  for(int i=0;i<SizeofPopulation;i++)
  {
   Population[i]=new String();
   for(int j=0;j<17;j++)
   {
    int randomallele=rand.nextInt(58);
    randomallele=randomallele+LowerBound;
    Sample[j]=(char) randomallele;
   }
   Population[i]=String.valueOf(Sample);
   FitnessOfPopulation[i]=calculateFitness(FitnessType, Population[i]);
  }
  for(int i=0;i<SizeofPopulation;i++)
  {
     if(HighestFitness<FitnessOfPopulation[i]) 
     {
         HighestFitness=FitnessOfPopulation[i];
     }
  }
  
}
void runAlgorithm() throws IOException
{
    initialPopulation();
    displayGeneration();
    NoOfGenerations++;
    if(RandomGenerationExperiment==0)
    {
    if(verifyZeroFitnessInPopulation()==100)
    {
       while(NoOfGenerations<2000000)
       {
           selection();
           crossOver();
           if(verifyZeroFitnessInPopulation()!=100)
           { break;}
            mutate();
          
           displayGeneration();
           if(verifyZeroFitnessInPopulation()!=100)
          { break;}
           NoOfGenerations++;
       }
    }
    if(visualize==1)
    {
    BufferedImage bi = DisplayMatrix;
    File outputfile = new File("saved.png");
    ImageIO.write(bi, "png", outputfile);
    ImageIcon icon=new ImageIcon(DisplayMatrix);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(2000,20000);
        JLabel lbl=new JLabel();
      lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    //ModifyImage();
            }
    else
    {
        int SetRandomNoOfGenerations=300;
        int LowestFitness=30000;
        String LeastString=new String();
        int LowestGeneration=0;
        int count=0;
        while(count!=300)
        {
            NoOfGenerations=count;
            initialPopulation();
            displayGeneration();
            for(int i=0;i<SizeofPopulation;i++)
            {
                if(LowestFitness>FitnessOfPopulation[i])
                {
                 LeastString=Population[i];
                 LowestFitness=FitnessOfPopulation[i]; 
                 LowestGeneration=count;
                }
            }
            count++;
        }
        System.out.println("Least String :"+LeastString);
        System.out.println("Lowest Fitness :"+LowestFitness);
        System.out.println("Lowest Generation :"+LowestGeneration);
        
    }
    
}
int verifyZeroFitnessInPopulation()
{
    for(int i=0;i<SizeofPopulation;i++)
    {
        if(FitnessOfPopulation[i]==0)
        {
            System.out.println("Converged !!!"+"Member Index :"+i+"  Member : "+Population[i]+" Fitness : "+ FitnessOfPopulation[i]);
            return(i);
            
        }
    }
  return(100);
}
void displayStatistics()
{
    
}
void displayGeneration()
{
    System.out.println("Population and Fitness of each Member of this Generation No: "+NoOfGenerations);
    for(int i=0;i<SizeofPopulation;i++)
    {
     System.out.println("Member Index :"+i+"  Member : "+Population[i]+" Fitness : "+ FitnessOfPopulation[i]);   
     if(visualize==1)
     {
     DisplayMatrix.setRGB(NoOfGenerations,FitnessOfPopulation[i],255);
     }
     }
}
void ModifyImage() throws IOException
{
    int rows=200;
    int columns=200;
    int x;
    int y;
    ReducedDisplayMatrix=new BufferedImage(rows,columns,TYPE_INT_RGB);
    int Range2=HighestFitness+1;
    int Range1=NoOfGenerations+1;
    for(int i=0;i<Range1;i++)
    {
        for(int j=0;j<Range2;j++)
        {
          
          x=(int)((rows)*(i/Range1));
          y=(int)((columns)*(j/Range2));
          y=columns-y;
          if(x>rows)
          {
              x=rows;
          }
          if(y>columns)
          {
              y=columns;
          }
          if(x<=0)
          {
              x=1;
          }
          if(y<=0)
          {
              y=1;
          }
          ReducedDisplayMatrix.setRGB(x-1, y-1,DisplayMatrix.getRGB(i, j));
          System.out.println("x="+x+"Y="+y+DisplayMatrix.getRGB(i, j));
        }
    }
    File outputfile = new File("saved.png");
    ImageIO.write(ReducedDisplayMatrix, "png", outputfile);
    ImageIcon icon=new ImageIcon(ReducedDisplayMatrix);
        JFrame frame=new JFrame();
        frame.setLayout(new FlowLayout());
        frame.setSize(200,200);
        JLabel lbl=new JLabel();
      lbl.setIcon(icon);
        frame.add(lbl);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}

}
