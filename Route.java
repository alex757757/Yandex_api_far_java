
	

import java.io.*;
	import java.util.*;
	import java.lang.*;
	import java.net.*;
	import org.json.simple.*;
	import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
	
public class Route {
	Point departure;
	Point arrival;
	String strroute;
	double duration;
	double distance;
	double durationInTraffic;
	
	Route()
	{
		departure = null;
		arrival = null;
		strroute = null;
		duration = 0;
		distance = 0;
		durationInTraffic = 0;
	}
	
	Route(Point a, Point b){
		a.GetCoordinates();
		departure = new Point(a);
		//departure.GetCoordinates();
		//System.out.println("departure/address = "+departure.address);
		//System.out.println("departure/coordinates[0] = "+departure.coordinates[0]);
		//System.out.println("departure/coordinates[1] = "+departure.coordinates[1]);
		b.GetCoordinates();
		arrival = new Point(b);
		//arrival.GetCoordinates();
		//System.out.println("arrival/address = "+arrival.address);
		//System.out.println("arrival/coordinates[0] = "+arrival.coordinates[0]);
		//System.out.println("arrival/coordinates[1] = "+arrival.coordinates[1]);
		duration = 0;
		distance = 0;
		durationInTraffic = 0;
	}
	
	public String GetUrl(){
		String urlName;
		urlName = ";";
		return urlName;
	}
	
	public void GetDuration(){
		try
		{
			String urlNameroute;
			//urlNameroute = "http://maps.yandex.ru/?rtext='Россия, Москва, улица Космонавтов, дом 7'~'Россия, Москва, улица Тверская, дом 6'&rtm=atm";
			urlNameroute = "http://maps.yandex.ru/?rtext=" + this.departure.coordinates[0] + "%2C" + this.departure.coordinates[1] + "~" + this.arrival.coordinates[0] + "%2C" + this.arrival.coordinates[1]+ "&rtm=amt";
			URL url = new URL(urlNameroute);
			URLConnection connection = url.openConnection();				
			Scanner in = new Scanner(connection.getInputStream());
			
			File flt = new File("D://request.json");
			FileWriter wrt = new FileWriter(flt);
			String strstr = "";
			while (in.hasNextLine()){
			//for(int n = 1; in.hasNextLine() && n<=30; n++)
				//String jsonreq = in.nextLine();
					//wrt.append(in.nextLine());
					//wrt.flush();
					//System.out.println(in.nextLine());
				 strstr += in.nextLine(); 
			}
			//System.out.println(strstr);
			String bb = "/json";
			int i = strstr.indexOf(bb)+7;
			int j = strstr.length();
			String newstr = strstr.substring(i, j);
			//System.out.println(newstr);
			i = newstr.indexOf("</script>");
			String newstrstr = newstr.substring(0, i);
			//System.out.println(newstrstr);
			wrt.append(newstrstr);
			wrt.flush();
			
			JSONParser parser = new JSONParser();
			 
			try {
		 
				//Object obj = parser.parse(new FileReader("D:\\request.json"));
				Object obj = parser.parse(newstrstr);
				//Object obj = parser.parse(new FileReader("D:\\request.json"));
				JSONObject jsonObject = (JSONObject) obj;
				JSONObject jv = (JSONObject) jsonObject.get("vpage");
				String name = (String) jv.get("name");
				//System.out.println(name);
				JSONObject jd = (JSONObject) jv.get("data");
				JSONObject jr = (JSONObject) jd.get("response");
				JSONObject js = (JSONObject) jr.get("data");
				JSONArray jss = (JSONArray) js.get("features");
				Iterator<JSONObject> iterator = jss.iterator();
				String stage;
				while (iterator.hasNext()) {
					stage = iterator.next().toString();
					//System.out.println(stage);
					Object object = parser.parse(stage); 
					JSONObject jsont = (JSONObject) object;
					JSONArray jsss = (JSONArray) jsont.get("features");
					Iterator<JSONObject> iter = jsss.iterator();
					String stages;
					while (iter.hasNext()) {
						stages = iter.next().toString();
						//System.out.println(stages);
						Object object1 = parser.parse(stages); 
						JSONObject jsont1 = (JSONObject) object;
						JSONObject jssss = (JSONObject) jsont1.get("properties");
						JSONObject jsssss = (JSONObject) jssss.get("RouteMetaData");
						JSONObject jssssss = (JSONObject)  jsssss.get("Distance");
						String jduu = (String) jssssss.get("value").toString();
						//System.out.println(jduu);
						this.distance = Double.parseDouble(jduu);
						JSONObject jssssss1 = (JSONObject)  jsssss.get("Duration");
						String jduu1 = (String) jssssss1.get("value").toString();
						//System.out.println(jduu1);
						this.duration = Double.parseDouble(jduu1);
						JSONObject jssssss2 = (JSONObject)  jsssss.get("DurationInTraffic");
						String jduu2 = (String) jssssss2.get("value").toString();
						//System.out.println(jduu2);
						this.durationInTraffic = Double.parseDouble(jduu2);
				}
			}
			} 
			catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Point a = new Point("Москва, Космонавтов улица, дом 7");
		Point b = new Point("Москва, Профсоюзная улица, дом 1");
		Route r = new Route(a, b);
		r.GetDuration();
		//System.out.println("Distance: "+r.distance);
		//System.out.println("Duration: "+r.duration);
		//System.out.println("DurationInTraffic: "+r.durationInTraffic);
	}

}
