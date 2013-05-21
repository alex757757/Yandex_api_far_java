
	import java.io.*;
	import java.util.*;
	import java.net.*;
	import javax.xml.parsers.DocumentBuilder;
	import javax.xml.parsers.DocumentBuilderFactory;
	import org.w3c.dom.*;

	public class Point {
		String address;
		float coordinates[];

		Point(String adr){
			address = new String(); 
			address = adr;
			coordinates = new float[2];
			coordinates[0]=0;
			coordinates[1]=0;
		}
		
		Point(Point a){
			address = new String();
			address = a.address;
			coordinates = new float[2];
			coordinates[0] = a.coordinates[0];
			coordinates[1] = a.coordinates[1];
		}
	public String GetUrl()
		{
			String urlName, str;
			char b = ',';
			char p = ' ';
			if (this.address.length() == 0){
				System.out.println("Haven't address string");
				urlName= " ";
			}
			else {
				str = this.address;
				urlName = "http://geocode-maps.yandex.ru/1.x/?geocode="+str.substring(0, str.indexOf(b));
				str = str.substring(str.indexOf(b)+1);
				urlName += str.substring(0, str.indexOf(p))+",+";
				str = str.substring(str.indexOf(p)+1);
				urlName += str.substring(0, str.indexOf(p))+"+";
				str = str.substring(str.indexOf(p)+1);
				urlName += str.substring(0, str.indexOf(b))+",+";
				str = str.substring(str.indexOf(b)+2);
				urlName += str.substring(0, str.indexOf(p))+"+";
				str = str.substring(str.indexOf(p)+1);
				urlName += str;
				//System.out.println(urlName);
				if(str.indexOf(b)>=0){
						urlName += ",+";
						str = str.substring(str.indexOf(b)+1);
						urlName += str.substring(0, str.indexOf(p))+"+";
						str= str.substring(str.indexOf(p)+1);
						urlName += str;	
				}
			}
			return urlName;
		}
		
		public void GetCoordinates(){
			try
			{
				String urlName;
				urlName = this.GetUrl();
				char p = ' ';
				System.out.println(urlName);
				URL url = new URL(urlName);
				URLConnection connection = url.openConnection();				
				Scanner in = new Scanner(connection.getInputStream());
				
				File flt = new File("D://req.xml");
				FileWriter wrt = new FileWriter(flt, true);
				while (in.hasNextLine())
				{
						wrt.append(in.nextLine());
						wrt.flush();
				}
		        try
		        {
		            DocumentBuilderFactory dbf=DocumentBuilderFactory.newInstance();
		            DocumentBuilder db=dbf.newDocumentBuilder();
		            Document doc=db.parse(urlName);
		            
		            doc.getDocumentElement().normalize();
		           // System.out.println("Root element ["+doc.getDocumentElement().getNodeName()+"]");
		            
		            NodeList nodeLst=doc.getElementsByTagName("Point");
		            //System.out.println("Points");
		            for(int je=0;je<nodeLst.getLength();je++)
		            {
		                Node fstNode=nodeLst.item(je);
		                if(fstNode.getNodeType()==Node.ELEMENT_NODE)
		                {
		                    Element elj=(Element)fstNode;
		                    NodeList nljx=elj.getElementsByTagName("pos");
		                    Element eljx=(Element)nljx.item(0);
		                    NodeList nljxc=eljx.getChildNodes();
		                    String coordinate = ((Node)nljxc.item(0)).getNodeValue();
		                    //System.out.println(coordinate);
		                    this.coordinates[0] = Float.parseFloat(coordinate.substring(0, coordinate.indexOf(p)));
		                    coordinate = coordinate.substring(coordinate.indexOf(p));
		                    this.coordinates[1] = Float.parseFloat(coordinate);
		                }
		            }
		        }
		        catch(Exception ei){}

				
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Point a = new Point("Москва, Вавилова улица, дом 3");
		a.GetCoordinates();
		Point c = new Point(a);
		System.out.println(c.address+","+c.coordinates[0]+";"+c.coordinates[1]);
	}

}
	
