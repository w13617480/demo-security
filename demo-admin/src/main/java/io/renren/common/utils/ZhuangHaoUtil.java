package io.renren.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.plaf.synth.SynthSpinnerUI;

public class ZhuangHaoUtil {
public static void main(String[] args) {
	System.out.println(isPositiveDecimal("1.2x"));
}
/**
 * 根据起始桩号生成20米弯沉检测点
 */
public static boolean isPositiveDecimal(String orginal){  
    return isMatch("\\+{0,1}[0]\\.[1-9]*|\\+{0,1}[1-9]\\d*\\.\\d*", orginal);  
} 
private static boolean isMatch(String regex, String orginal){  
    if (orginal == null || orginal.trim().equals("")) {  
        return false;  
    }  
    Pattern pattern = Pattern.compile(regex);  
    Matcher isNum = pattern.matcher(orginal);  
    return isNum.matches();  
} 
public static boolean isNumeric1(String str){
	  for (int i = 0; i < str.length(); i++){
	   System.out.println(str.charAt(i));
	   if (!Character.isDigit(str.charAt(i))){
	    return false;
	   }
	  }
	  return true;
	 }
public static List erShi(String qd,String zdd,String cd1){

//	List list1=baiMi("k177+154","k178+000");
//	for(int x=0;x<list1.size();x++){
//		System.out.println(list1.get(x));
//	}
	
    String cd=cd1;
	String zh=qd;
	String zd=zdd;
	Map map=huoQuZH(zh);
	Map map1=huoQuZH(zd);
	 String zdsz=map1.get("sz").toString();
     String zdzm=map1.get("zm").toString();
     if(zdsz.indexOf(".")!=-1){
    	 
     }else{
    	 zdsz=zdsz+".0";
     }
     String[] zdszs=zdsz.split("\\.");
     String zdsz1=zdszs[1];
     String h=map.get("sz").toString();
     String zm=map.get("zm").toString();
     if(h.indexOf(".")!=-1){
    	 
     }else{
    	h= h+".0";
     }
     String[] hs=h.split("\\.");
     String i=hs[1];
     int su=(int) Math.ceil(Double.parseDouble(cd)/20);
    int z=Integer.parseInt(hs[0]);
     String d="0."+i;
     double dd=Double.parseDouble(d);
     if(dd==0.1){
    	 dd=dd+0.01;
     }else if(dd==0.2){
    	 dd=dd+0.01;
     }else if(dd==0.3){
    	 dd=dd+0.01;
     }else if(dd==0.4){
    	 dd=dd+0.01;
     }else if(dd==0.5){
    	 dd=dd+0.01;
     }else if(dd==0.6){
    	 dd=dd+0.01;
     }else if(dd==0.7){
    	 dd=dd+0.01;
     }else if(dd==0.8){
    	 dd=dd+0.01;
     }else if(dd==0.9){
    	 dd=dd+0.01;
     }
    double c=(dd)*10;
    
    double u= Math.ceil(c);
    List list =new ArrayList();
    String s=hs[0];
    int ss=Integer.parseInt(s);
    int sh=ss+1;
    int j =jianCha(zh);
    double zds=Double.parseDouble(zdsz);
    int f=20;
    if(Integer.parseInt(cd)<1000){

    	for(int x=0;x<su;x++){
    		int k=x*20;
    		if(k<90){
    			if(k==0){
    				if(j==1){
    					list.add(zm+s+"+"+"00"+x*20+"~"+zm+s+"+"+"0"+(x+1)*20);
    				}else if(j==2){
    					list.add(zm+s+"."+"00"+x*20+"~"+zm+s+"."+"0"+(x+1)*20);
    				}else{
    					list.add(zm+s+"."+"00"+x*20+"~"+zm+s+"."+"0"+(x+1)*20);
    				}
    				
    			}else {
    				if((x+1)*20==100){
    					if(j==1){
    						list.add(zm+s+"+"+"0"+x*20+"~"+zm+s+"+"+(x+1)*20);
    					}else if(j==2){
    						list.add(zm+s+"."+"0"+x*20+"~"+zm+s+"."+(x+1)*20);
    					}else{
    						list.add(zm+s+"."+"0"+x*20+"~"+zm+s+"."+(x+1)*20);
    					}
    					
    				}else{
    					if(j==1){
    						list.add(zm+s+"+"+"0"+x*20+"~"+zm+s+"+"+"0"+(x+1)*20);
    					}else if(j==2){
    						list.add(zm+s+"."+"0"+x*20+"~"+zm+s+"."+"0"+(x+1)*20);
    					}else{
    						list.add(zm+s+"."+"0"+x*20+"~"+zm+s+"."+"0"+(x+1)*20);
    					}
    						
    				}
    				
    			}
    				
    		}else{
    			if(k==100||(x+1)*20==100){
    				if(j==1){
    					list.add(zm+s+"+"+x*20+"~"+zm+s+"+"+(x+1)*20);	
    				}else if(j==2){
    					list.add(zm+s+"."+x*20+"~"+zm+s+"."+(x+1)*20);	
    				}else{
    					list.add(zm+s+"."+x*20+"~"+zm+s+"."+(x+1)*20);	
    				}
    				
    			}else if((x+1)*20>Integer.parseInt(cd)){
    				if(j==1){
    					list.add(zm+s+"+"+x*20+"~"+zd);	
    				}else if(j==2){
    					list.add(zm+s+"."+x*20+"~"+zd);	
    				}else{
    					list.add(zm+s+"."+x*20+"~"+zd);	
    				}
    				
    			}else {
    				if(j==1){
    					list.add(zm+s+"+"+x*20+"~"+zm+s+"+"+(x+1)*20);	
    				}else if(j==2){
    					list.add(zm+s+"."+x*20+"~"+zm+s+"."+(x+1)*20);	
    				}else{
    					list.add(zm+s+"."+x*20+"~"+zm+s+"."+(x+1)*20);	
    				}
    				
    			}
    		
    		}
    		
    	}
    
    	
    } else if(Integer.parseInt(cd)==1000){
    	for(int x=0;x<su;x++){
    		int k=x*20;
    		if(k<90){
    			if(k==0){
    				if(j==1){
    					list.add(zm+s+"+"+"00"+x*20+"~"+zm+s+"+"+"0"+(x+1)*20);
    				}else if(j==2){
    					list.add(zm+s+"."+"00"+x*20+"~"+zm+s+"."+"0"+(x+1)*20);
    				}else{
    					list.add(zm+s+"."+"00"+x*20+"~"+zm+s+"."+"0"+(x+1)*20);
    				}
    				
    			}else {
    				if((x+1)*20==100){
    					if(j==1){
    						list.add(zm+s+"+"+"0"+x*20+"~"+zm+s+"+"+(x+1)*20);
    					}else if(j==2){
    						list.add(zm+s+"."+"0"+x*20+"~"+zm+s+"."+(x+1)*20);
    					}else{
    						list.add(zm+s+"."+"0"+x*20+"~"+zm+s+"."+(x+1)*20);
    					}
    					
    				}else{
    					if(j==1){
    						list.add(zm+s+"+"+"0"+x*20+"~"+zm+s+"+"+"0"+(x+1)*20);
    					}else if(j==2){
    						list.add(zm+s+"."+"0"+x*20+"~"+zm+s+"."+"0"+(x+1)*20);
    					}else{
    						list.add(zm+s+"."+"0"+x*20+"~"+zm+s+"."+"0"+(x+1)*20);
    					}
    						
    				}
    				
    			}
    				
    		}else{
    			if(k==100||(x+1)*20==100){
    				if(j==1){
    					list.add(zm+s+"+"+x*20+"~"+zm+s+"+"+(x+1)*20);	
    				}else if(j==2){
    					list.add(zm+s+"."+x*20+"~"+zm+s+"."+(x+1)*20);	
    				}else{
    					list.add(zm+s+"."+x*20+"~"+zm+s+"."+(x+1)*20);	
    				}
    				
    			}else if((x+1)*20==1000){
    				if(j==1){
    					list.add(zm+s+"+"+x*20+"~"+zd);	
    				}else if(j==2){
    					list.add(zm+s+"."+x*20+"~"+zd);	
    				}else{
    					list.add(zm+s+"."+x*20+"~"+zd);	
    				}
    				
    			}else {
    				if(j==1){
    					list.add(zm+s+"+"+x*20+"~"+zm+s+"+"+(x+1)*20);	
    				}else if(j==2){
    					list.add(zm+s+"."+x*20+"~"+zm+s+"."+(x+1)*20);	
    				}else{
    					list.add(zm+s+"."+x*20+"~"+zm+s+"."+(x+1)*20);	
    				}
    				
    			}
    		
    		}
    		
    	}
    } else if(sh<zds){
    	double l=0.2;
    	int jihu=0;
    	int zs=0;
    	int gr=0;
    	for(int x=0;x<su;x++){
    		int k=x*20;
    		
    		if(k<(1000-c*100)){
    			if(c*100>100){
    				
    				
    	    			if((c+(x+1)*l)<=Math.ceil(c)){
    	    			
    	    				int gh=(int)(dd*1000+(x+1)*20);
    	    				
    	    				if(k==0){
    	    					
    	    					if(j==1){
    	    						list.add(zh+"~"+zm+s+"+"+gh);
    	    					}else if(j==2){
    	    						list.add(zh+"~"+zm+s+"."+gh);	
    	    					}else{
    	    						list.add(zh+"~"+zm+s+"."+gh);
    	    					}
    	    					
    	    				}else{
    	    					int gh1=(int)(dd*1000+(x+2)*20);
    	    					if(gh1>100&&gh1<120){
    	    						gh1=100;
    	    					}else if(gh1>200&&gh1<220){
    	    						gh1=200;
    	    					}else if(gh1>300&&gh1<320){
    	    						gh1=300;
    	    					}else if(gh1>400&&gh1<420){
    	    						gh1=400;
    	    					}else if(gh1>500&&gh1<520){
    	    						gh1=500;
    	    					}else if(gh1>600&&gh1<620){
    	    						gh1=600;
    	    					}else if(gh1>700&&gh1<720){
    	    						gh1=700;
    	    					}else if(gh1>800&&gh1<820){
    	    						gh1=800;
    	    					}else if(gh1>900&&gh1<920){
    	    						gh1=900;
    	    					}
    	    					zs=gh1;
    	    					gr=x;
    	    					if(gh1<1000){
    	    						if(j==1){
    	    							list.add(zm+s+"+"+gh+"~"+zm+s+"+"+gh1);
    	    						}else if(j==2){
    	    							list.add(zm+s+"."+gh+"~"+zm+s+"."+gh1);
    	    						}else{
    	    							list.add(zm+s+"."+gh+"~"+zm+s+"."+gh1);
    	    						}
    	    						
    	    					}else{
    	    						if(j==1){
    	    							list.add(zm+s+"+"+gh+"~"+zm+sh+"+"+"000");
    	    						}else if(j==2){
    	    							list.add(zm+s+"."+gh+"~"+zm+sh+"."+"000");
    	    						}else{
    	    							list.add(zm+s+"."+gh+"~"+zm+sh+"."+"000");
    	    						}
    	    						
    	    					}
    	    					
    	    				}
    	    					
    	        				
    	        			
    	    			}else{
    	    				int gl=zs+(x-gr)*20;
    	    				int gj=zs+(x-gr-1)*20;
    	    				if(gl<1000){
    	    				if(j==1){
    	    					list.add(zm+s+"+"+gj+"~"+zm+s+"+"+gl);
    	    				}else if(j==2){
    	    					
    	    					list.add(zm+s+"."+gj+"~"+zm+s+"."+gl);
    	    				}else{
    	    					list.add(zm+s+"."+gj+"~"+zm+s+"."+gl);
    	    				}
    	    			 }else if(gj<1000){
    	    				 if(j==1){
     	    					list.add(zm+s+"+"+gj+"~"+zm+sh+"+"+"000");
     	    				}else if(j==2){
     	    					
     	    					list.add(zm+s+"."+gj+"~"+zm+sh+"."+"000");
     	    				}else{
     	    					list.add(zm+s+"."+gj+"~"+zm+sh+"."+"000");
     	    				} 
    	    			 }
    	    			}
    	    	
    			}
    			
    			jihu=x;
    		}else{
        		 k=(x-jihu-1)*20;
        		if(k<90){
        			if(k==0){
        				if(j==1){
        					list.add(zm+sh+"+"+"00"+(x-jihu-1)*20+"~"+zm+sh+"+"+"0"+(x-jihu)*20);
        				}else if(j==2){
        					list.add(zm+sh+"."+"00"+(x-jihu-1)*20+"~"+zm+sh+"."+"0"+(x-jihu)*20);
        				}else{
        					list.add(zm+sh+"."+"00"+(x-jihu-1)*20+"~"+zm+sh+"."+"0"+(x-jihu)*20);
        				}
        				
        			}else {
        				if((x-jihu)*20==100){
        					if(j==1){
        						list.add(zm+sh+"."+"0"+(x-jihu-1)*20+"~"+zm+sh+"."+(x-jihu)*20);
            				}else if(j==2){
            					list.add(zm+sh+"."+"0"+(x-jihu-1)*20+"~"+zm+sh+"."+(x-jihu)*20);
            				}else{
            					list.add(zm+sh+"."+"0"+(x-jihu-1)*20+"~"+zm+sh+"."+(x-jihu)*20);
            				}
        					
        				}else{
        					if(j==1){
        						list.add(zm+sh+"+"+"0"+(x-jihu-1)*20+"~"+zm+sh+"+"+"0"+(x-jihu)*20);
            				}else if(j==2){
            					list.add(zm+sh+"."+"0"+(x-jihu-1)*20+"~"+zm+sh+"."+"0"+(x-jihu)*20);
            				}else{
            					list.add(zm+sh+"."+"0"+(x-jihu-1)*20+"~"+zm+sh+"."+"0"+(x-jihu)*20);
            				}
        						
        				}
        				
        			}
        				
        		}else{
        			if(k==100||(x-jihu)*20==100){
        				if(j==1){
        					list.add(zm+sh+"+"+(x-jihu-1)*20+"~"+zm+sh+"+"+(x-jihu)*20);
        				}else if(j==2){
        					list.add(zm+sh+"."+(x-jihu-1)*20+"~"+zm+sh+"."+(x-jihu)*20);	
        				}else{
        					list.add(zm+sh+"."+(x-jihu-1)*20+"~"+zm+sh+"."+(x-jihu)*20);	
        				}
        					
        			}else if((x-jihu)*20==1000){
        				if(j==1){
        					list.add(zm+sh+"+"+(x-jihu-1)*20+"~"+zd);	
        				}else if(j==2){
        					list.add(zm+sh+"."+(x-jihu-1)*20+"~"+zd);	
        				}else{
        					list.add(zm+sh+"."+(x-jihu-1)*20+"~"+zd);	
        				}
        				
        			}else if((x-jihu)*20<1000){
        				if(j==1){
        					list.add(zm+sh+"+"+(x-jihu-1)*20+"~"+zm+sh+"+"+(x-jihu)*20);
        				}else if(j==2){
        					list.add(zm+sh+"."+(x-jihu-1)*20+"~"+zm+sh+"."+(x-jihu)*20);
        				}else{
        					list.add(zm+sh+"."+(x-jihu-1)*20+"~"+zm+sh+"."+(x-jihu)*20);
        				}
        					
        			}
        		
        		}
        		
        	
    		}
    		
    	}
    	
    }else{

    	for(int x=0;x<su;x++){
    		int k=x*20;
    		if(k<90){
    			if(k==0){
    				if(j==1){
    					list.add(zm+s+"+"+"00"+x*20+"~"+zm+s+"+"+"0"+(x+1)*20);
    				}else if(j==2){
    					list.add(zm+s+"."+"00"+x*20+"~"+zm+s+"."+"0"+(x+1)*20);
    				}else{
    					list.add(zm+s+"."+"00"+x*20+"~"+zm+s+"."+"0"+(x+1)*20);
    				}
    				
    			}else {
    				if((x+1)*20==100){
    					if(j==1){
    						list.add(zm+s+"+"+"0"+x*20+"~"+zm+s+"+"+(x+1)*20);
    					}else if(j==2){
    						list.add(zm+s+"."+"0"+x*20+"~"+zm+s+"."+(x+1)*20);
    					}else{
    						list.add(zm+s+"."+"0"+x*20+"~"+zm+s+"."+(x+1)*20);
    					}
    					
    				}else{
    					if(j==1){
    						list.add(zm+s+"+"+"0"+x*20+"~"+zm+s+"+"+"0"+(x+1)*20);
    					}else if(j==2){
    						list.add(zm+s+"."+"0"+x*20+"~"+zm+s+"."+"0"+(x+1)*20);
    					}else{
    						list.add(zm+s+"."+"0"+x*20+"~"+zm+s+"."+"0"+(x+1)*20);
    					}
    						
    				}
    				
    			}
    				
    		}else{
    			if(k==100||(x+1)*20==100){
    				if(j==1){
    					list.add(zm+s+"+"+x*20+"~"+zm+s+"+"+(x+1)*20);	
    				}else if(j==2){
    					list.add(zm+s+"."+x*20+"~"+zm+s+"."+(x+1)*20);	
    				}else{
    					list.add(zm+s+"."+x*20+"~"+zm+s+"."+(x+1)*20);	
    				}
    				
    			}else if((x+1)*20>Integer.parseInt(cd)){
    				if(j==1){
    					list.add(zm+s+"+"+x*20+"~"+zd);	
    				}else if(j==2){
    					list.add(zm+s+"."+x*20+"~"+zd);	
    				}else{
    					list.add(zm+s+"."+x*20+"~"+zd);	
    				}
    				
    			}else {
    				if(j==1){
    					list.add(zm+s+"+"+x*20+"~"+zm+s+"+"+(x+1)*20);	
    				}else if(j==2){
    					list.add(zm+s+"."+x*20+"~"+zm+s+"."+(x+1)*20);	
    				}else{
    					list.add(zm+s+"."+x*20+"~"+zm+s+"."+(x+1)*20);	
    				}
    				
    			}
    		
    		}
    		
    	}
    
    }
   
	return list;

}
/**
 * 根据起始桩号生成百米弯沉检测点
 */
public static List baiMi(String qd,String zdd){
	String zh=qd;
	String zd=zdd;
	Map map=huoQuZH(zh);
	Map map1=huoQuZH(zd);
	 String zdsz=map1.get("sz").toString();
     String zdzm=map1.get("zm").toString();
     if(zdsz.indexOf(".")!=-1){
    	 
     }else{
    	 zdsz=zdsz+".0";
     }
     String[] zdszs=zdsz.split("\\.");
     String zdsz1=zdszs[1];
     String h=map.get("sz").toString();
     String zm=map.get("zm").toString();
     if(h.indexOf(".")!=-1){
    	 
     }else{
    	 h=h+".0";
     }
     String[] hs=h.split("\\.");
     String i=hs[1];
    int z=Integer.parseInt(hs[0]);
     String d="0."+i;
     double dd=Double.parseDouble(d);
     if(dd==0.1){
    	 dd=dd+0.01;
     }else if(dd==0.2){
    	 dd=dd+0.01;
     }else if(dd==0.3){
    	 dd=dd+0.01;
     }else if(dd==0.4){
    	 dd=dd+0.01;
     }else if(dd==0.5){
    	 dd=dd+0.01;
     }else if(dd==0.6){
    	 dd=dd+0.01;
     }else if(dd==0.7){
    	 dd=dd+0.01;
     }else if(dd==0.8){
    	 dd=dd+0.01;
     }else if(dd==0.9){
    	 dd=dd+0.01;
     }
    double c=(dd)*10;
    
    double u= Math.ceil(c);
    List list =new ArrayList();
    String s=hs[0];
    int ss=Integer.parseInt(s);
    int sh=ss+1;
    int j =jianCha(zh);
    double zds=Double.parseDouble(zdsz);
    if(sh==zds){
     if(u==0){
    	if(j==1){
    		list.add(zh+"~"+zm+s+"+"+100);
    		list.add(zm+s+"+"+100+"~"+zm+s+"+"+200);
    		list.add(zm+s+"+"+200+"~"+zm+s+"+"+300);
    		list.add(zm+s+"+"+300+"~"+zm+s+"+"+400);
    		list.add(zm+s+"+"+400+"~"+zm+s+"+"+500);
    		list.add(zm+s+"+"+500+"~"+zm+s+"+"+600);
    		list.add(zm+s+"+"+600+"~"+zm+s+"+"+700);
    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
    	}else if(j==2){
    		list.add(zh+"~"+zm+s+"."+100);
    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}else{
    		list.add(zh+"~"+zm+s+"."+100);
    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}
    }else if(u==1){
    	if(j==1){
    		list.add(zh+"~"+zm+s+"+"+100);
    		list.add(zm+s+"+"+100+"~"+zm+s+"+"+200);
    		list.add(zm+s+"+"+200+"~"+zm+s+"+"+300);
    		list.add(zm+s+"+"+300+"~"+zm+s+"+"+400);
    		list.add(zm+s+"+"+400+"~"+zm+s+"+"+500);
    		list.add(zm+s+"+"+500+"~"+zm+s+"+"+600);
    		list.add(zm+s+"+"+600+"~"+zm+s+"+"+700);
    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
    	}else if(j==2){
    		list.add(zh+"~"+zm+s+"."+100);
    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}else{
    		list.add(zh+"~"+zm+s+"."+100);
    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}
    	
    }else if(u==2){

    	if(j==1){
    	
    		list.add(zh+"~"+zm+s+"+"+200);
    		list.add(zm+s+"+"+200+"~"+zm+s+"+"+300);
    		list.add(zm+s+"+"+300+"~"+zm+s+"+"+400);
    		list.add(zm+s+"+"+400+"~"+zm+s+"+"+500);
    		list.add(zm+s+"+"+500+"~"+zm+s+"+"+600);
    		list.add(zm+s+"+"+600+"~"+zm+s+"+"+700);
    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
    	}else if(j==2){
    		list.add(zh+"~"+zm+s+"."+200);
    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}else{
    		list.add(zh+"~"+zm+s+"."+200);
    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}
    	
    	
    }else if(u==3){


    	if(j==1){
    	
    		
    		list.add(zh+"~"+zm+s+"+"+300);
    		list.add(zm+s+"+"+300+"~"+zm+s+"+"+400);
    		list.add(zm+s+"+"+400+"~"+zm+s+"+"+500);
    		list.add(zm+s+"+"+500+"~"+zm+s+"+"+600);
    		list.add(zm+s+"+"+600+"~"+zm+s+"+"+700);
    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
    	}else if(j==2){
    		list.add(zh+"~"+zm+s+"."+300);
    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}else{
    		list.add(zh+"~"+zm+s+"."+300);
    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}
    	
    	
    
    }else if(u==4){

    	if(j==1){
    		list.add(zh+"~"+zm+s+"+"+400);
    		list.add(zm+s+"+"+400+"~"+zm+s+"+"+500);
    		list.add(zm+s+"+"+500+"~"+zm+s+"+"+600);
    		list.add(zm+s+"+"+600+"~"+zm+s+"+"+700);
    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
    	}else if(j==2){
    		list.add(zh+"~"+zm+s+"."+400);
    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}else{
    		
    		list.add(zh+"~"+zm+s+"."+400);
    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}
    	
    	
    
    
    }else if(u==5){


    	if(j==1){
    		list.add(zh+"~"+zm+s+"+"+500);
    		list.add(zm+s+"+"+500+"~"+zm+s+"+"+600);
    		list.add(zm+s+"+"+600+"~"+zm+s+"+"+700);
    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
    	}else if(j==2){
    		list.add(zh+"~"+zm+s+"."+500);
    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}else{
    		
    	
    		list.add(zh+"~"+zm+s+"."+500);
    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}
    	
    	
    
    
    
    }else if(u==6){



    	if(j==1){
    		list.add(zh+"~"+zm+s+"+"+600);
    		list.add(zm+s+"+"+600+"~"+zm+s+"+"+700);
    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
    	}else if(j==2){
    		list.add(zh+"~"+zm+s+"."+600);
    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}else{
    	
    		list.add(zh+"~"+zm+s+"."+600);
    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}
    	
    	
    
    
    
    
    }else if(u==7){

    	if(j==1){
    		list.add(zh+"~"+zm+s+"+"+700);
    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
    	}else if(j==2){
    		list.add(zh+"~"+zm+s+"."+700);
    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}else{
    	
    		list.add(zh+"~"+zm+s+"."+700);
    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}
    
    }else if(u==8){
    	if(j==1){
    		list.add(zh+"~"+zm+s+"+"+800);
    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
    	}else if(j==2){
    		list.add(zh+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}else{
    	
    		list.add(zh+"~"+zm+s+"."+800);
    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}
    
    
    }else if(u==9){

    	if(j==1){
    		list.add(zh+"~"+zm+s+"+"+900);
    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
    	}else if(j==2){
    		list.add(zh+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}else{
    		list.add(zh+"~"+zm+s+"."+900);
    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
    	}
    
    }else{
    	if(j==1){
    		
    		list.add(zh+"~"+zm+sh+"+"+"000");
    	}else if(j==2){
    		list.add(zh+"~"+zm+sh+"."+"000");
    	}else{
    		list.add(zh+"~"+zm+sh+"."+"000");
    	}
    }
   } else if(sh<zds){

	     if(u==0){
	    	if(j==1){
	    		list.add(zh+"~"+zm+s+"+"+100);
	    		list.add(zm+s+"+"+100+"~"+zm+s+"+"+200);
	    		list.add(zm+s+"+"+200+"~"+zm+s+"+"+300);
	    		list.add(zm+s+"+"+300+"~"+zm+s+"+"+400);
	    		list.add(zm+s+"+"+400+"~"+zm+s+"+"+500);
	    		list.add(zm+s+"+"+500+"~"+zm+s+"+"+600);
	    		list.add(zm+s+"+"+600+"~"+zm+s+"+"+700);
	    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
	    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
	    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}else{
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}
	    }else if(u==1){
	    	if(j==1){
	    		list.add(zh+"~"+zm+s+"+"+100);
	    		list.add(zm+s+"+"+100+"~"+zm+s+"+"+200);
	    		list.add(zm+s+"+"+200+"~"+zm+s+"+"+300);
	    		list.add(zm+s+"+"+300+"~"+zm+s+"+"+400);
	    		list.add(zm+s+"+"+400+"~"+zm+s+"+"+500);
	    		list.add(zm+s+"+"+500+"~"+zm+s+"+"+600);
	    		list.add(zm+s+"+"+600+"~"+zm+s+"+"+700);
	    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
	    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
	    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}else{
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}
	    	
	    }else if(u==2){

	    	if(j==1){
	    	
	    		list.add(zh+"~"+zm+s+"+"+200);
	    		list.add(zm+s+"+"+200+"~"+zm+s+"+"+300);
	    		list.add(zm+s+"+"+300+"~"+zm+s+"+"+400);
	    		list.add(zm+s+"+"+400+"~"+zm+s+"+"+500);
	    		list.add(zm+s+"+"+500+"~"+zm+s+"+"+600);
	    		list.add(zm+s+"+"+600+"~"+zm+s+"+"+700);
	    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
	    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
	    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}else{
	    		list.add(zh+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}
	    	
	    	
	    }else if(u==3){


	    	if(j==1){
	    	
	    		
	    		list.add(zh+"~"+zm+s+"+"+300);
	    		list.add(zm+s+"+"+300+"~"+zm+s+"+"+400);
	    		list.add(zm+s+"+"+400+"~"+zm+s+"+"+500);
	    		list.add(zm+s+"+"+500+"~"+zm+s+"+"+600);
	    		list.add(zm+s+"+"+600+"~"+zm+s+"+"+700);
	    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
	    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
	    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}else{
	    		list.add(zh+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}
	    	
	    	
	    
	    }else if(u==4){

	    	if(j==1){
	    		list.add(zh+"~"+zm+s+"+"+400);
	    		list.add(zm+s+"+"+400+"~"+zm+s+"+"+500);
	    		list.add(zm+s+"+"+500+"~"+zm+s+"+"+600);
	    		list.add(zm+s+"+"+600+"~"+zm+s+"+"+700);
	    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
	    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
	    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}else{
	    		
	    		list.add(zh+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}
	    	
	    	
	    
	    
	    }else if(u==5){


	    	if(j==1){
	    		list.add(zh+"~"+zm+s+"+"+500);
	    		list.add(zm+s+"+"+500+"~"+zm+s+"+"+600);
	    		list.add(zm+s+"+"+600+"~"+zm+s+"+"+700);
	    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
	    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
	    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}else{
	    		
	    	
	    		list.add(zh+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}
	    	
	    	
	    
	    
	    
	    }else if(u==6){



	    	if(j==1){
	    		list.add(zh+"~"+zm+s+"+"+600);
	    		list.add(zm+s+"+"+600+"~"+zm+s+"+"+700);
	    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
	    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
	    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}else{
	    	
	    		list.add(zh+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}
	    	
	    	
	    
	    
	    
	    
	    }else if(u==7){

	    	if(j==1){
	    		list.add(zh+"~"+zm+s+"+"+700);
	    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
	    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
	    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}else{
	    	
	    		list.add(zh+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}
	    
	    }else if(u==8){
	    	if(j==1){
	    		list.add(zh+"~"+zm+s+"+"+800);
	    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
	    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}else{
	    	
	    		list.add(zh+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}
	    
	    
	    }else if(u==9){

	    	if(j==1){
	    		list.add(zh+"~"+zm+s+"+"+900);
	    		list.add(zm+s+"+"+900+"~"+zm+sh+"+"+"000");
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}else{
	    		list.add(zh+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zm+sh+"."+"000");
	    	}
	    
	    }else{
	    	if(j==1){
	    		
	    		list.add(zh+"~"+zm+sh+"+"+"000");
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+sh+"."+"000");
	    	}else{
	    		list.add(zh+"~"+zm+sh+"."+"000");
	    	}
	    }
	   
   	if(j==1){
   		list.add(zm+sh+"+"+"000"+"~"+zm+sh+"+"+100);
   		list.add(zm+sh+"+"+100+"~"+zm+sh+"+"+200);
   		list.add(zm+sh+"+"+200+"~"+zm+sh+"+"+300);
   		list.add(zm+sh+"+"+300+"~"+zm+sh+"+"+400);
   		list.add(zm+sh+"+"+400+"~"+zm+sh+"+"+500);
   		list.add(zm+sh+"+"+500+"~"+zm+sh+"+"+600);
   		list.add(zm+sh+"+"+600+"~"+zm+sh+"+"+700);
   		list.add(zm+sh+"+"+700+"~"+zm+sh+"+"+800);
   		list.add(zm+sh+"+"+800+"~"+zm+sh+"+"+900);
   		list.add(zm+sh+"+"+900+"~"+zd);
   	}else if(j==2){
   		list.add(zm+sh+"."+"000"+"~"+zm+sh+"."+100);
   		list.add(zm+sh+"."+100+"~"+zm+sh+"."+200);
   		list.add(zm+sh+"."+200+"~"+zm+sh+"."+300);
   		list.add(zm+sh+"."+300+"~"+zm+sh+"."+400);
   		list.add(zm+sh+"."+400+"~"+zm+sh+"."+500);
   		list.add(zm+sh+"."+500+"~"+zm+sh+"."+600);
   		list.add(zm+sh+"."+600+"~"+zm+sh+"."+700);
   		list.add(zm+sh+"."+700+"~"+zm+sh+"."+800);
   		list.add(zm+sh+"."+800+"~"+zm+sh+"."+900);
   		list.add(zm+sh+"."+900+"~"+zd);
   	}else{
   		list.add(zm+sh+"."+"000"+"~"+zm+sh+"."+100);
   		list.add(zm+sh+"."+100+"~"+zm+sh+"."+200);
   		list.add(zm+sh+"."+200+"~"+zm+sh+"."+300);
   		list.add(zm+sh+"."+300+"~"+zm+sh+"."+400);
   		list.add(zm+sh+"."+400+"~"+zm+sh+"."+500);
   		list.add(zm+sh+"."+500+"~"+zm+sh+"."+600);
   		list.add(zm+sh+"."+600+"~"+zm+sh+"."+700);
   		list.add(zm+sh+"."+700+"~"+zm+sh+"."+800);
   		list.add(zm+sh+"."+800+"~"+zm+sh+"."+900);
   		list.add(zm+s+"."+900+"~"+zd);
   	}
   
   }else{
	
	  double x=Math.abs(zds-sh)*10;
	  double g= Math.ceil(x);
	 if(g==9){

		  if(j==1){
		   list.add(zh+"~"+zm+s+"+"+100);
		   list.add(zm+s+"+"+100+"~"+zd);
		 		
		  }else if(j==2){
			  list.add(zh+"~"+zm+s+"."+100);
			   list.add(zm+s+"."+100+"~"+zd);
		  }else{
			  list.add(zh+"~"+zm+s+"."+100);
			   list.add(zm+s+"."+100+"~"+zd);
		 } 
	  
	  }else if(g==8){

	    	if(j==1){
	    		list.add(zh+"~"+zm+s+"+"+100);
	    		list.add(zm+s+"+"+100+"~"+zm+s+"+"+200);
	    		list.add(zm+s+"+"+200+"~"+zd);
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zd);
	    	}else{
	    	
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zd);
	    	}
	    
	    
	    
	  }else if(g==7){


	    	if(j==1){
	    		list.add(zh+"~"+zm+s+"+"+100);
	    		list.add(zm+s+"+"+100+"~"+zm+s+"+"+200);
	    		list.add(zm+s+"+"+200+"~"+zm+s+"+"+300);
	    		list.add(zm+s+"+"+300+"~"+zd);
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zd);
	    	}else{
	    	
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zd);
	    	}
	    
	    
	  }else if(g==6){

	    	if(j==1){
	    		list.add(zh+"~"+zm+s+"+"+100);
	    		list.add(zm+s+"+"+100+"~"+zm+s+"+"+200);
	    		list.add(zm+s+"+"+200+"~"+zm+s+"+"+300);
	    		list.add(zm+s+"+"+300+"~"+zm+s+"+"+400);
	    		list.add(zm+s+"+"+400+"~"+zd);
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zd);
	    	}else{
	    	
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zd);
	    	}
	    
	  }else if(g==5){

	    	if(j==1){
	    		list.add(zh+"~"+zm+s+"+"+100);
	    		list.add(zm+s+"+"+100+"~"+zm+s+"+"+200);
	    		list.add(zm+s+"+"+200+"~"+zm+s+"+"+300);
	    		list.add(zm+s+"+"+300+"~"+zm+s+"+"+400);
	    		list.add(zm+s+"+"+400+"~"+zm+s+"+"+500);
	    		list.add(zm+s+"+"+500+"~"+zd);
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zd);
	    	}else{
	    		
	    	
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zd);
	    	}
	    
	  }else if(g==4){


	    	if(j==1){
	    		list.add(zh+"~"+zm+s+"+"+100);
	    		list.add(zm+s+"+"+100+"~"+zm+s+"+"+200);
	    		list.add(zm+s+"+"+200+"~"+zm+s+"+"+300);
	    		list.add(zm+s+"+"+300+"~"+zm+s+"+"+400);
	    		list.add(zm+s+"+"+400+"~"+zm+s+"+"+500);
	    		list.add(zm+s+"+"+500+"~"+zm+s+"+"+600);
	    		list.add(zm+s+"+"+600+"~"+zd);
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zd);
	    	}else{
	    		
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zd);
	    	}
	  
	  }else if(g==3){



	    	if(j==1){
	    	
	    		
	    		list.add(zh+"~"+zm+s+"+"+100);
	    		list.add(zm+s+"+"+100+"~"+zm+s+"+"+200);
	    		list.add(zm+s+"+"+200+"~"+zm+s+"+"+300);
	    		list.add(zm+s+"+"+300+"~"+zm+s+"+"+400);
	    		list.add(zm+s+"+"+400+"~"+zm+s+"+"+500);
	    		list.add(zm+s+"+"+500+"~"+zm+s+"+"+600);
	    		list.add(zm+s+"+"+600+"~"+zm+s+"+"+700);
	    		list.add(zm+s+"+"+700+"~"+zd);
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zd);
	    	}else{
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zd);
	    	}
	    
	  }else if(g==2){


	    	if(j==1){
	    	
	    		list.add(zh+"~"+zm+s+"+"+100);
	    		list.add(zm+s+"+"+100+"~"+zm+s+"+"+200);
	    		list.add(zm+s+"+"+200+"~"+zm+s+"+"+300);
	    		list.add(zm+s+"+"+300+"~"+zm+s+"+"+400);
	    		list.add(zm+s+"+"+400+"~"+zm+s+"+"+500);
	    		list.add(zm+s+"+"+500+"~"+zm+s+"+"+600);
	    		list.add(zm+s+"+"+600+"~"+zm+s+"+"+700);
	    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
	    		list.add(zm+s+"+"+800+"~"+zd);
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zd);
	    	}else{
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zd);
	    	}
	    
	  }else if(g==1){

	    	if(j==1){
	    		list.add(zh+"~"+zm+s+"+"+100);
	    		list.add(zm+s+"+"+100+"~"+zm+s+"+"+200);
	    		list.add(zm+s+"+"+200+"~"+zm+s+"+"+300);
	    		list.add(zm+s+"+"+300+"~"+zm+s+"+"+400);
	    		list.add(zm+s+"+"+400+"~"+zm+s+"+"+500);
	    		list.add(zm+s+"+"+500+"~"+zm+s+"+"+600);
	    		list.add(zm+s+"+"+600+"~"+zm+s+"+"+700);
	    		list.add(zm+s+"+"+700+"~"+zm+s+"+"+800);
	    		list.add(zm+s+"+"+800+"~"+zm+s+"+"+900);
	    		list.add(zm+s+"+"+900+"~"+zd);
	    	}else if(j==2){
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zd);
	    	}else{
	    		list.add(zh+"~"+zm+s+"."+100);
	    		list.add(zm+s+"."+100+"~"+zm+s+"."+200);
	    		list.add(zm+s+"."+200+"~"+zm+s+"."+300);
	    		list.add(zm+s+"."+300+"~"+zm+s+"."+400);
	    		list.add(zm+s+"."+400+"~"+zm+s+"."+500);
	    		list.add(zm+s+"."+500+"~"+zm+s+"."+600);
	    		list.add(zm+s+"."+600+"~"+zm+s+"."+700);
	    		list.add(zm+s+"."+700+"~"+zm+s+"."+800);
	    		list.add(zm+s+"."+800+"~"+zm+s+"."+900);
	    		list.add(zm+s+"."+900+"~"+zd);
	    	}
	    	
	    
	  }
	   
	 
   }
   
	
   return list;
}

/**  
 * 判断字符串是否包含#
 *   
 * @param str  
 * @return  
 */ 
public static int jianCha(String zd){
	int i=0;
	if(zd.indexOf("+")>-1){
		i=1;
		return i;
	}else if(zd.indexOf(".")>-1){
		i=2;
		return i;
	}else {
		return 3;
	}
	
	
}
/**  
 * 转化字符串把#转化为. 
 *   
 * @param str  
 * @return  
 */ 
public static String zhuanHua(String zd){
	String i="";
	if(zd.indexOf("+")>-1){
		i=zd.replaceAll("\\+",".");
	}
	return i;
}
/**  
 *   用split分割后组合成一个字符串只有一个点
 *   
 * @param str  
 * @return  
 */ 
public static String zuHe(String zd){
	String i="";
	
	if(zd.indexOf(".")>-1){
		
		String[] is=zd.split("\\.");
		if(is.length==1){
			i=is[0];
	  }else if(is.length==2){
		  i=is[0]+"."+is[1];
	  }else if(is.length==3){
		  i=is[0]+"."+is[1]+is[2];
	  }
	
		
	}else{
		i=zd;
	}
	return i;
}


/**  
 * 判断字符串是否为数字  
 *   
 * @param str  
 * @return  
 */  
public static boolean isNumeric(String str) {  
  
    return true;  
}  
/**  
 * 截取字母
 *   
 * @param str  
 * @return  
 */  
public static Map ziMu(String str) {  
	Map map=new HashMap();
	String zm="";
	String zm1="";
	String sz="";
    if(!isNumeric(str)){
    	zm=str.substring(0,1);
    	String st=str.substring(1, str.length());
    	sz=str.substring(1, str.length());
    	if(!isNumeric(st)){
    		zm1=st.substring(0, 1);
    		sz=st.substring(1, st.length());
    	}
    }else{
    	sz=str;
    }
    map.put("zm", zm+zm1);
    map.put("sz", sz);
    return map;  
} 
/**  
 * 根据传入的桩号分别获取
 *   
 * @param str  
 * @return  
 */  

public static Map huoQuZH(String zh){
	Map map=null;
	
	if(jianCha(zh)==1){
		
		String xzh=zhuanHua(zh);
		String xzf=zuHe(xzh);
		//获取桩号前面所有字母，默认获取前两个
		 map=ziMu(xzf);
		 map.put("dian", 1);
	}else if(jianCha(zh)==2){
		
		String xzf=zuHe(zh);
		//获取桩号前面所有字母，默认获取前两个
		 map=ziMu(xzf);
		 map.put("dian", 2);
	}else if(jianCha(zh)==3){
		 map=ziMu(zh);
		 map.put("dian", 3);
		
		
	}
	return map;
}
}
