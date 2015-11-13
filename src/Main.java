import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Main {

	
	private static boolean fail = false;
	private static boolean leftFail = false;
	private static boolean leftOk = false;
	private static boolean problem = true;	
	private static int v = 0;
	private static int n = 0;
	private static int m = 0;
	
	private static String path = "res/LP-1.txt";
	private static String newPath = "res/LP-2.txt";
	
	private static ArrayList<String> temp_sintelestes_antikeimenikis_sinartisis = new ArrayList<String>();
	private static ArrayList<String> temp_deiktes_antikeimenikis_sinartisis = new ArrayList<String>();
	
	private static String[][] A;
	private static String[] b;
	private static String[] c;
	private static String[] Equin;
	private static int MinMax = 0;

	public static void main(String[] args) {

		CheckAndCreate();

	}

	public static boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
			return false; 
		}
		// only got here if we didn't return false
		return true;
	}
	public static boolean Checking() {

		try{
			FileInputStream fstream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;
			boolean ready = true;

			while ((strLine = br.readLine()) != null)   {
				if(!strLine.trim().isEmpty())  {
					char[] c = strLine.toCharArray();

					if(ready) {
						ready = false;

						int y = 0;
						int lastIndex = 0;;
						int firstIndex;
						int firstMPosition = 0;
						boolean stop = false;
						String firstM = Character.toString(c[y]);


						while(!stop) {
							if(!firstM.equals("m")) {
								if(firstM.equals(" ")) y++;
								else {
									fail = true;
									stop = true;
								}
							}else {
								stop = true;
								firstMPosition = y;
							}
							firstM = Character.toString(c[y]);
						}

						String firstI_A = Character.toString(c[firstMPosition + 1]);
						String firstN_X = Character.toString(c[firstMPosition + 2]);

						if(!fail && ((firstI_A.equals("i") && firstN_X.equals("n")) || (firstI_A.equals("a") && firstN_X.equals("x")))) {
							if(firstI_A.equals("a") && firstN_X.equals("x")) {
								MinMax = 1;
							}else MinMax = -1;


							String lastElement;
							firstIndex = firstMPosition+3;
							for(int k = firstIndex;k<c.length;k++) {
								lastElement = Character.toString(c[y]);
								if(!lastElement.equals(" ")) {
									lastIndex = k - 2;
								}
							}

							if(!ExpressionsAreCorrect(firstIndex,lastIndex)) fail = true;
							else fail = false;			

						}else fail = true;				
					}
				}
			}
			in.close();

		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

		return fail;

	}
	public static boolean ExpressionsAreCorrect(int startFirstRow,int endFirstRow) {

		try{ 
			FileInputStream fstream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			char[] c;
			
			String strLine;
			String gen= "xj>=0(j=1,2...n)";
			String prosimo = "";

			int i = 0;			
			int naturePos = 0;
			int first_number_position = 0;
			int start = 0;
			int leftStart = 0;
			int endCheck = 0;
			int xIntNumber = 0;

			boolean restrictions = false;
			boolean ok;
			boolean diplo;

			ArrayList<String> deiktesMinOrMax = new ArrayList<String>();


			while ((strLine = br.readLine()) != null)   {

				if(!strLine.trim().isEmpty()) { 

					ArrayList<String> sintelestes = new ArrayList<String>();
					ok = false;
					problem = true;
					diplo = false;

					c = strLine.toCharArray();

					String row = strLine.replaceAll("\\s+","");
					if(!row.equals(gen)) {
						if(i==1) {	
							int s_t_position = 0;
							String temp_s_t = Character.toString(c[s_t_position]);		
							boolean stop = false;
							while(s_t_position < c.length && !fail && !stop) {
								if(temp_s_t.equals(" ")) s_t_position++;
								else {
									if(temp_s_t.equals("s")) {
										String dot_temp_s_t = Character.toString(c[s_t_position+1]);
										String t_temp_s_t = Character.toString(c[s_t_position+2]);
										String dot2_temp_s_t = Character.toString(c[s_t_position+3]);
										String em_temp_s_t =  Character.toString(c[s_t_position+4]);
										if(dot_temp_s_t.equals(".") && t_temp_s_t.equals("t") && dot2_temp_s_t.equals(".") && em_temp_s_t.equals(" ")) {
											v = s_t_position + 5;	
											stop = true;
										}
										else fail = true; 
									}else if(!temp_s_t.equals("s")) fail = true;											
								}
								temp_s_t = Character.toString(c[s_t_position]);	
							}
						}else v = 0;

						//start right to midle check----------------------------------------------------------------------------------------------------------------------------
						if(restrictions && !fail) {			
							String myStringNumber = "";

							int k = c.length-1;
							while(k>-1 && !fail && problem &&!leftFail) {
								String temp = Character.toString(c[k]);
								String nextTemp = Character.toString(c[k-1]);
								myStringNumber+=temp;
								if(isInteger(temp) && !isInteger(nextTemp) ) {
									problem = false;
									first_number_position = k;
									if(nextTemp.equals("+") || nextTemp.equals("-")) {
										first_number_position--;
										diplo = true;
										if(nextTemp.equals("+"))myStringNumber="+" +myStringNumber;
										else myStringNumber= "-" +myStringNumber ;
									}
								}
								if(!temp.equals(" ") && !isInteger(temp)) {
									fail = true;
								}
								k--;					
							}


							boolean diploSimbolo = false;

							if(!problem && !fail) {
								start = first_number_position - 1; 
								while(start>0 && !fail && !ok) {
									String temp = Character.toString(c[start]);
									String leftTemp = Character.toString(c[start-1]);
									if(!temp.equals(" ")) {
										if(!temp.equals("=") && !temp.equals("<") && !temp.equals(">")) {
											fail = true;
											System.out.println("Problem in "+i+" row (integer or error symbol found)");
										}else  {
											if((temp.equals("=") || temp.equals(">") || temp.equals("<")) && (leftTemp.equals(" ")|| isInteger(leftTemp))) {
												ok = true;
											}else if(temp.equals("=") && leftTemp.equals("<")) {										
												ok = true;
												diploSimbolo = true;
											}else if(temp.equals("=") && leftTemp.equals(">")) {										
												ok = true;
												diploSimbolo = true;
											}else {
												fail = true;
												System.out.println("Problem in "+i+" row");
											}
										} 
									}
									start--;
								}
							}//end right to midle  checking-----------------------------------------------------------------------------------------------------------------------------

							//start checking midle to left-----------------------------------------------------------------------------------------------------------------------------
							if(ok && !fail) {

								if(diplo) leftStart = start - 1;									
								else leftStart = start;

								endCheck = leftStart;
								String temp;
								String leftTemp;
								while(leftStart>1 && !leftFail && !leftOk) {
									temp = Character.toString(c[leftStart]);
									leftTemp = Character.toString(c[leftStart-1]);
									if(!temp.equals(" ")) {
										if(!isInteger(temp)) leftFail = true;
										if(leftTemp.equals("x") && isInteger(temp)) leftOk = true;	
									}
									leftStart--;
								}

							}//end checking midle to left-----------------------------------------------------------------------------------------------------------------------------		

							String temp;
							String nextTemp;
							String xStringNumber = "";
							String sintelestisString ="";

							int count = 0;

							boolean stop = false;
							boolean integer = true;
							boolean keno = false;
							boolean checkProsimo = false;

							//start checking left to midle----------------------------------------------------------------------------------------------------------------------------
							if(ok && leftOk && !fail) { //restrictions  rows

								System.out.println("grammi "+i+" -------------");

								if(diploSimbolo) endCheck--;
								while(v <= endCheck && !fail) {

									temp = Character.toString(c[v]);
									nextTemp = Character.toString(c[v+1]);

									if(!temp.equals(" ") && !temp.equals("+") && !temp.equals("-") && !isInteger(temp) && !temp.equals("x")) {
										fail = true;
										System.out.println("Unknown Symbol");
									}
									else if(checkProsimo && (!temp.equals("+") && !temp.equals("-") && !temp.equals(" "))) {
										fail = true;
										System.out.println("Den brethike prosimo meta tin metabliti");
									}
									else {
										if(temp.equals("+") || temp.equals("-")) { 					
											if(count != 0 && !checkProsimo) {//meta to prwto prosimo
												System.out.println("Meta to prwto prosimo");
												if(isInteger(nextTemp) && !checkProsimo) {
													integer = true;
													boolean epi = false;
													startFirstRow++;
													temp = Character.toString(c[v]);
													if(temp.equals("*")) epi = true;
													while(!epi && integer && !temp.equals("x")) {
														if(isInteger(temp)){
															sintelestisString+=temp;
															v++;
														}
														else if(temp.equals("*")) {
															epi = true;
															v++;
														}
														else integer = false;																							
														temp = Character.toString(c[v]);
														nextTemp = Character.toString(c[v + 1]);
													}
													if(temp.equals("x") && integer) { // -4x or -4*x	
														if(temp.equals("*")) v++;
														v++;
														temp = Character.toString(c[v]);
														stop = false;
														xStringNumber = "";
														if(!isInteger(temp)) fail =  true;
														while(isInteger(temp) && !stop && !fail) {	
															xStringNumber+=temp;									
															v++;
															temp = Character.toString(c[v]);
															if(temp.equals(" ") || !isInteger(temp)) stop = true;
														}
														System.out.println(sintelestisString);
														if(!fail && Integer.parseInt(sintelestisString)!=0) {
															xIntNumber = Integer.parseInt(xStringNumber);
															if(n<xIntNumber) n = xIntNumber;
															if(sintelestes.size() != 0) {
																boolean foundSintelesti = false;
																for(int t = 0;t<sintelestes.size();t++) {
																	if(xStringNumber.equals(sintelestes.get(t)) && !foundSintelesti) {
																		System.out.println("Brethike idios sintelestis");
																		foundSintelesti = true;
																		fail = true;
																	}
																}
																if(xIntNumber<Integer.parseInt(sintelestes.get(sintelestes.size()-1)) || xIntNumber==0) fail = true;
																if(!foundSintelesti && !fail){
																	System.out.println("Eisagwgi oxi prwtou steixeiou sti lista(res)");
																	sintelestes.add(xStringNumber);	
																	count++;
																}else System.out.println("Apotixia eisagwgi oxi prwtou steixeiou sti lista(res)");																
															}
															else {
																System.out.println("Eisagwgi prwtou stoixeiou sti lista(res)");																											
																if(xIntNumber!=0)sintelestes.add(xStringNumber);
																else {
																	fail = true;
																	System.out.println("o deiktis einai 0");

																}
															}		
														}
														xStringNumber="";
														count++;
														checkProsimo = true;
													}else fail = true;
												}else{ 
													System.out.println("Metabliti xwris arithmo i aplo prosimo");
													if(nextTemp.equals(" ") && checkProsimo) {
														v++;
														temp = Character.toString(c[v]);
														keno = true;
													}
													if(!temp.equals("x") && !keno && !checkProsimo) {
														fail = true;
													}	

												}
											}else if(count == 0){ //elegxos gia to an brei prwto prosimo
												if(isInteger(nextTemp) || nextTemp.equals("x")) {
													count++;
													v++;
												}
												else fail = true;	
												System.out.println("Prwto prosimo");
											}else if(count!=0 && checkProsimo) {
												checkProsimo = false;
												v++;
											}
										}else if(temp.equals("x")  && !checkProsimo) {
											v++;
											temp = Character.toString(c[v]);
											stop = false;
											xStringNumber = "";
											if(!isInteger(temp)) fail =  true;
											while(isInteger(temp) && !stop && !fail) {	
												xStringNumber+=temp;									
												v++;
												temp = Character.toString(c[v]);
												if(temp.equals(" ") || !isInteger(temp)) stop = true;
											}
											if(!fail) {
												xIntNumber = Integer.parseInt(xStringNumber);
												if(n<xIntNumber) n = xIntNumber;
												if(sintelestes.size() != 0) {
													boolean foundSintelesti = false;
													for(int t = 0;t<sintelestes.size();t++) {
														if(xStringNumber.equals(sintelestes.get(t)) && !foundSintelesti) {
															System.out.println("Brethike idios sintelestis");
															foundSintelesti = true;
															fail = true;
														}
													}
													if(xIntNumber<Integer.parseInt(sintelestes.get(sintelestes.size()-1))) fail = true;
													if(!foundSintelesti && !fail){
														System.out.println("Eisagwgi oxi prwtou stoixeiou sti lista(res)");
														sintelestes.add(xStringNumber);	
														count++;	
														checkProsimo = true;
													}else System.out.println("Apotixia eisagwgi oxi prwtou stoixeiou sti lista(res)");												
												}
												else {
													System.out.println("Eisagwgi prwtou stoixeiou sti lista(res)");																											
													if(xIntNumber!=0)sintelestes.add(xStringNumber);
													else {
														fail = true;
														System.out.println("o deiktis einai 0");

													}
												}		
											}
											count++;	
											checkProsimo = true;
										}
										else if(isInteger(temp) && !checkProsimo) count++;		
										else if(temp.equals(" ")) v++;						

										if(isInteger(temp) && count>0 && !checkProsimo) {//+ 2x or + x

											System.out.println("Arithmos endiamesa");
											fail = false;
											integer = true;
											boolean epi = false;
											startFirstRow++;
											temp = Character.toString(c[v]);
											nextTemp = Character.toString(c[v+1]);

											while(!epi && integer && !temp.equals("x")) {
												if(isInteger(temp)) {
													sintelestisString+=temp;
													v++;	
												}
												else if(temp.equals("*")) {
													epi = true;
													v++;
												}
												else {
													integer = false;																							
												}
												temp = Character.toString(c[v]);
												nextTemp = Character.toString(c[v+1]);
											}
											if(temp.equals("x") && integer) { 	
												v++;
												temp = Character.toString(c[v]);
												stop = false;
												xStringNumber = "";
												if(!isInteger(temp)) fail =  true;
												while(isInteger(temp) && !stop && !fail) {	
													xStringNumber+=temp;									
													v++;
													temp = Character.toString(c[v]);
													if(temp.equals(" ") || !isInteger(temp)) stop = true;
												}

												if(!fail && !sintelestisString.equals("0")) {
													xIntNumber = Integer.parseInt(xStringNumber);
													if(n<xIntNumber) n = xIntNumber;
													if(sintelestes.size() != 0) {
														boolean foundSintelesti = false;
														for(int t = 0;t<sintelestes.size();t++) {
															if(xStringNumber.equals(sintelestes.get(t)) && !foundSintelesti) {
																System.out.println("Brethike idios sintelestis");
																foundSintelesti = true;
																fail = true;
															}
														}								
														if(xIntNumber<Integer.parseInt(sintelestes.get(sintelestes.size()-1))) {
															System.out.println("megaliteros deiktis");
															fail = true;
														}
														if(!foundSintelesti && !fail){
															System.out.println("Eisagwgi oxi prwtou stoixeiou sti lista(res)");
															sintelestes.add(xStringNumber);	
															count++;
															checkProsimo = true;
														}else{ 
															System.out.println("Apotixia eisagwgis oxi prwtou stoixeiou sti lista(res)");	
														}
													}
													else {
														System.out.println("Eisagwgi prwtou stoixeiou sti lista(res)");																											
														if(xIntNumber!=0)sintelestes.add(xStringNumber);
														else {
															fail = true;
															System.out.println("o deiktis einai 0");

														}	
													}	
												}
												sintelestisString = "";
												count++;
												checkProsimo = true;
											}else fail = true;
										}	
									}
								}//end of the row	
							}// check ok and left ok
						}// check if it is an restriction
						else if(!restrictions && !fail) {// min or max row 
							System.out.println("prwti grammi me min i max -------------");

							String temp;
							String nextTemp;
							String xStringNumber = "";
							
							int count = 0;
							
							boolean keno = false;
							boolean checkProsimo = false;
							boolean stop = false;
							boolean integer = true;
							boolean epi = false;
							boolean OkFirstTime = false;


							while(startFirstRow <= endFirstRow && !fail) {

								temp = Character.toString(c[startFirstRow]);
								nextTemp = Character.toString(c[startFirstRow+1]);
								String sintelestis = "";


								if(OkFirstTime) {
									if(!temp.equals(" ") && !temp.equals("+") && !temp.equals("-") && !isInteger(temp) && !temp.equals("x")) {
										fail = true;
										System.out.println("Unknown Symbol");
									}
									else if(checkProsimo && (!temp.equals("+") && !temp.equals("-") && !temp.equals(" "))) {
										fail = true;
										System.out.println("Den brethike prosimo meta tin metabliti");
									}
									else {
										if(temp.equals("+") || temp.equals("-")) {
											if(temp.equals("-")) prosimo = temp;
											if(!(count == 0) && !checkProsimo) {//meta to prwto prosimo
												System.out.println("Meta to prwto prosimo");
												if(isInteger(nextTemp) && !checkProsimo) {		
													integer = true;
													epi = false;
													startFirstRow++;
													temp = Character.toString(c[startFirstRow]);
													if(temp.equals("*")) epi = true;
													while(!epi && integer && !temp.equals("x")) {
														if(isInteger(temp)) {
															sintelestis+=temp;
															startFirstRow++;	
														}
														else if(temp.equals("*")) epi = true;
														else integer = false;																							
														temp = Character.toString(c[startFirstRow]);
														nextTemp = Character.toString(c[startFirstRow + 1]);
													}

													if(!sintelestis.equals("0")){
														sintelestis = prosimo + sintelestis;
														temp_sintelestes_antikeimenikis_sinartisis.add(sintelestis);
													}
													prosimo = "";
													if((temp.equals("x")||(temp.equals("*") && nextTemp.equals("x"))) && integer) { 	
														if(temp.equals("*")) startFirstRow++;
														startFirstRow++;
														temp = Character.toString(c[startFirstRow]);
														stop = false;
														xStringNumber = "";
														if(!isInteger(temp)) fail =  true;
														while(isInteger(temp) && !stop && !fail) {	
															xStringNumber+=temp;									
															startFirstRow++;
															temp = Character.toString(c[startFirstRow]);
															if(temp.equals(" ") || !isInteger(temp)) stop = true;
														}
														if(!fail && !sintelestis.equals("0")) {																											
															xIntNumber = Integer.parseInt(xStringNumber);	
															if(n<xIntNumber) n = xIntNumber;
															if(deiktesMinOrMax.size() != 0) {
																boolean foundSintelesti = false;
																for(int t = 0;t<deiktesMinOrMax.size();t++) {
																	if((xStringNumber.equals(deiktesMinOrMax.get(t)) && !foundSintelesti)) {
																		foundSintelesti = true;
																		fail = true;
																	}
																}
																if(xIntNumber<Integer.parseInt(deiktesMinOrMax.get(deiktesMinOrMax.size()-1))) fail = true;
																if(!foundSintelesti && !fail){
																	System.out.println("Eisagwgi oxi prwtou steixeiou sti lista");
																	temp_deiktes_antikeimenikis_sinartisis.add(xStringNumber);
																	deiktesMinOrMax.add(xStringNumber);	
																	count++;
																}
															}
															else {
																System.out.println("Eisagwgi prwtou steixeiou sti lista");												
																if(xIntNumber!=0) {
																	deiktesMinOrMax.add(xStringNumber);
																	temp_deiktes_antikeimenikis_sinartisis.add(xStringNumber);
																}
																else {
																	fail = true;
																	System.out.println("o deiktis einai 0");

																}	
															}		
														}
														count++;
														checkProsimo = true;
													}else fail = true;
												}else{ 
													System.out.println("Metabliti xwris arithmo i aplo prosimo");
													if(nextTemp.equals(" ") && checkProsimo) {
														startFirstRow++;
														temp = Character.toString(c[startFirstRow]);
														keno = true;
													}
													if(!temp.equals("x") && !keno && !checkProsimo) {
														fail = true;
													}	

												}
											}else if(count == 0){ //elegxos gia to an brei prwto prosimo
												if(isInteger(nextTemp) || nextTemp.equals("x")) {
													count++;
													startFirstRow++;
												}
												else fail = true;	
												System.out.println("Prwto prosimo");
											}else if(count!=0 && checkProsimo) {
												checkProsimo = false;
												startFirstRow++;
											}
										}else if(temp.equals("x")  && !checkProsimo) {
											sintelestis = prosimo + "1";
											temp_sintelestes_antikeimenikis_sinartisis.add(sintelestis);
											prosimo = "";
											startFirstRow++;
											temp = Character.toString(c[startFirstRow]);
											stop = false;
											xStringNumber = "";
											if(!isInteger(temp) && !temp.equals("*")) fail =  true;
											if(temp.equals("*")) startFirstRow++;
											while(isInteger(temp) && !stop && !fail) {	
												xStringNumber+=temp;									
												startFirstRow++;
												temp = Character.toString(c[startFirstRow]);
												if(temp.equals(" ") || !isInteger(temp)) stop = true;
											}
											if(!fail && !sintelestis.equals("0")) {
												xIntNumber = Integer.parseInt(xStringNumber);
												if(n<xIntNumber) n = xIntNumber;
												if(deiktesMinOrMax.size() != 0) {
													boolean foundSintelesti = false;
													for(int t = 0;t<deiktesMinOrMax.size();t++) {
														if(xStringNumber.equals(deiktesMinOrMax.get(t)) && !foundSintelesti) {
															System.out.println("Brethike idios sintelestis");
															foundSintelesti = true;
															fail = true;
														}
													}
													if(xIntNumber<Integer.parseInt(deiktesMinOrMax.get(deiktesMinOrMax.size()-1))) fail = true;
													if(!foundSintelesti && !fail){
														System.out.println("Eisagwgi oxi prwtou stoixeiou sti lista(min or max)");
														temp_deiktes_antikeimenikis_sinartisis.add(xStringNumber);
														deiktesMinOrMax.add(xStringNumber);	
														count++;	
														checkProsimo = true;
													}
												}
												else {
													System.out.println("Eisagwgi prwtou stoixeiou sti lista(min or max)");
													if(xIntNumber!=0) {
														deiktesMinOrMax.add(xStringNumber);
														temp_deiktes_antikeimenikis_sinartisis.add(xStringNumber);
													}
													else {
														fail = true;
														System.out.println("o deiktis einai 0");

													}	

												}		
											}
											count++;	
											checkProsimo = true;
										}
										else if(isInteger(temp) && !checkProsimo) {
											if(!sintelestis.equals("0")) sintelestis = prosimo + temp;
											prosimo = "";
											count++;		
										}
										else if(temp.equals(" ")) {
											startFirstRow++;
										}

										if(isInteger(temp) && count>0 && !checkProsimo) {//+ 2x or + x

											System.out.println("Arithmos endiamesa");
											fail = false;
											integer = true;
											epi = false;
											startFirstRow++;
											temp = Character.toString(c[startFirstRow]);
											nextTemp = Character.toString(c[startFirstRow+1]);
											System.out.println(sintelestis);

											while(integer && !temp.equals("x")) {
												if(isInteger(temp)) {
													sintelestis+=temp;
													startFirstRow++;
												}
												else if(temp.equals("*")) {
													epi = true;
													startFirstRow++;
												}
												else integer = false;

												temp = Character.toString(c[startFirstRow]);
												nextTemp = Character.toString(c[startFirstRow+1]);
											}
											
											if(Integer.parseInt(sintelestis)!=0) {	
												sintelestis = prosimo + sintelestis;
												temp_sintelestes_antikeimenikis_sinartisis.add(sintelestis);
											}
											prosimo = "";
											if(temp.equals("x") && (integer||epi)) {
												startFirstRow++;
												temp = Character.toString(c[startFirstRow]);
												stop = false;
												xStringNumber = "";
												if(!isInteger(temp)) fail =  true;
												while(isInteger(temp) && !stop && !fail) {	
													//System.out.println("hi!");
													xStringNumber+=temp;									
													startFirstRow++;
													temp = Character.toString(c[startFirstRow]);
													if(temp.equals(" ") || !isInteger(temp)) stop = true;
												}	
												if(!fail && (!sintelestis.equals("0") && !sintelestis.equals("-0"))) {
													xIntNumber = Integer.parseInt(xStringNumber);
													if(n<xIntNumber) n = xIntNumber;
													if(deiktesMinOrMax.size() != 0) {
														boolean foundSintelesti = false;
														for(int t = 0;t<deiktesMinOrMax.size();t++) {
															if(xStringNumber.equals(deiktesMinOrMax.get(t)) && !foundSintelesti) {
																foundSintelesti = true;
																fail = true;
															}
														}
														if(xIntNumber<Integer.parseInt(deiktesMinOrMax.get(deiktesMinOrMax.size()-1))) fail = true;
														if(!foundSintelesti && !fail){
															System.out.println("Eisagwgi oxi prwtou stoixeiou sti lista(min or max)");
															temp_deiktes_antikeimenikis_sinartisis.add(xStringNumber);
															deiktesMinOrMax.add(xStringNumber);	
															count++;
															checkProsimo = true;
														}else{ 
															System.out.println("Apotixia eisagwgi oxi prwtou stoixeiou sti lista(min or max)");									
														}
													}
													else {
														System.out.println("Eisagwgi prwtou stoixeiou sti lista");	
														if(xIntNumber!=0) {
															deiktesMinOrMax.add(xStringNumber);
															temp_deiktes_antikeimenikis_sinartisis.add(xStringNumber);
														}
														else {
															fail = true;
															System.out.println("o deiktis einai 0");
														}	
													}
												}
												sintelestis = "";
												count++;
												checkProsimo = true;
											}else fail = true;									
										}
									}
								}
								else {
									OkFirstTime = true;
									if(!temp.equals(" ")) fail = true;
								}	
							}
							restrictions = true;
						}//min or max row
						i++;
						m = i - 1;
					}else {//check for nature restriction
						naturePos = i;
						i++;
					}
				}//check if line is blank
			}//end file reading/start checking left to midle-------------------------------------------------------------------------------------------------------------------------------

			if(naturePos<m) {
				fail = true;
				System.out.println("Sfalma thesis i morfis tou fisikou periorismou");
			}

			in.close();

		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

		if(fail || leftFail) return false;
		else if(!problem && leftOk) return true;
		else return true;

	}
	public static void loadArrays() {

		b = new String [m];
		Equin = new String[m];
		c = new String[n];
		A = new String[m][n]; 

		for(int i = 0;i<m;i++) {
			b[i] = "0";
			Equin[i] = "0";
			for(int j = 0;j<n;j++) {
				c[j] = "0";
				A[i][j] = "0";
			}
		}


		try{

			FileInputStream fstream = new FileInputStream(path);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));

			String strLine;
			String gen= "xj>=0(j=1,2...n)";

			boolean firstLine = true;
			boolean s_t = false;

			int line = 0;

			while ((strLine = br.readLine()) != null)   {
				String row = strLine.replaceAll("\\s+","");
				if(!strLine.trim().isEmpty() && !row.equals(gen)) {

					boolean beforeX = true;
					boolean numberFound = false;

					char[] c = strLine.toCharArray();

					int pos = 0;		
					int dotPos = 0;
					int dotCount = 0;

					String numberBefore = "";
					String numberAfter = "";
					String temp = Character.toString(c[pos]);
					String nextTemp = Character.toString(c[pos+1]);
					String prosimoSint = "";

					if(!firstLine){
						//periorismoi						
						if(s_t) {//grammi s_t
							temp = Character.toString(c[dotPos]);
							while(dotCount!=2 && dotPos < c.length) {					
								if(temp.equals(".")) {
									dotCount++;
									pos = dotPos;								
								}
								dotPos++;
								temp = Character.toString(c[dotPos]);						
							}					
							s_t = false;
						}else pos = 0;

						while(!temp.equals("=") && !temp.equals("<") && !temp.equals(">")) {
							if(temp.equals(" ") || temp.equals("+") || temp.equals("-") || temp.equals("*")) {
								if(temp.equals("-")) prosimoSint = "-";
								pos++;
							}
							else if(isInteger(temp)) {
								if(beforeX) {
									numberBefore+=temp;
									numberFound = true;
								}
								else {
									numberAfter+=temp;
									if(!isInteger(nextTemp)) {
										if(!numberBefore.equals("0")) A[line][Integer.parseInt(numberAfter)-1] = prosimoSint + numberBefore;
										prosimoSint = "";
										beforeX = true;	
										numberFound = false;
										numberBefore = "";
										numberAfter = "";
									}
								}
								pos++;
							}
							else if(temp.equals("x")) {
								if(!numberFound) numberBefore = "1";
								beforeX = false;					
								pos++;
							}

							temp = Character.toString(c[pos]);
							nextTemp = Character.toString(c[pos+1]);
						}

						if(temp.equals(">") || temp.equals("<")) {
							if(nextTemp.equals("=")) {
								pos+=2;
								if(temp.equals(">")) Equin[line] = "1";
								else Equin[line] = "-1";
							}else {
								pos++;
								if(temp.equals(">")) Equin[line] = "1";
								else Equin[line] = "-1";
							}
						}else {
							pos++;
							Equin[line] = "0";
						}

						temp = Character.toString(c[pos]);
						while(temp.equals(" ")) {
							pos++;
							temp = Character.toString(c[pos]);
							nextTemp = Character.toString(c[pos+1]);
						}

						String prosimo = "";
						String rightNumber = "";

						if(temp.equals("+") || temp.equals("-")) {
							prosimo = temp;
							pos++;
						}else{
							prosimo = "";
						}

						temp = Character.toString(c[pos]);

						while(isInteger(temp)) {
							rightNumber+=temp;
							pos++;
							temp = Character.toString(c[pos]);	
						}

						b[line] = prosimo + rightNumber;

						line++;
					}else{//prwti grammi
						firstLine = false;
						s_t = true;
					}

				}
			}
			in.close();

			for(int i = 0;i<temp_deiktes_antikeimenikis_sinartisis.size();i++) {
				c[Integer.parseInt(temp_deiktes_antikeimenikis_sinartisis.get(i)) - 1] = temp_sintelestes_antikeimenikis_sinartisis.get(i); 
			}

		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

		System.out.println("Ok with loading...");
	}
	public static void createFileLP2() {

		try {

			File file = new File(newPath);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			int[] gaps = new int[n];

			if(MinMax == 1)bw.write("max[1]");
			else bw.write("min[-1]"); 

			int maxSize = c[0].length();

			for(int i = 1;i<n;i++) {
				if(c[i].length() > maxSize) maxSize = c[i].length();
			}

			for(int i = 0;i<n;i++)	{
				int kena = maxSize-c[i].length();
				if(i == n/2){
					bw.write("          |"+c[i] + Gaps(kena) + "| x"); 
				}	
				else if(i==0) {
					bw.write("    |"+c[i] + Gaps(kena) + "|");
				}
				else {
					bw.write("          |"+ c[i] + Gaps(kena) + "|");
				}
				bw.write("\n");
			}

			bw.write("\n\n");
			bw.write("s.t.      ");
			//emfanisi pinakwn meta to s.t.--------------------------------------------------------------------------------------------------------------------------

			int maxSize2D; 
			for(int i = 0;i<n;i++) {
				maxSize2D = A[0][i].length();
				for(int j = 1;j<m;j++) {
					if(A[j][i].length() > maxSize2D) maxSize2D = A[j][i].length();
				}	
				gaps[i] = maxSize2D;
			}

			int maxSizeb;
			maxSizeb = b[0].length();
			for(int j = 1;j<m;j++) {
				if(b[j].length() > maxSizeb) maxSizeb = b[j].length();
			}	

			for(int i=0;i<m;i++) {
				for(int j = 0;j<n;j++)	{
					int kena = gaps[j] - A[i][j].length() + 3;
					if(i==0) {
						if(j==0) {
							bw.write("|"+A[i][j] + Gaps(kena));
						}
						else if(j == n-1) {
							int finKena = gaps[j] -  A[i][j].length();
							bw.write(A[i][j] + Gaps(finKena)+"|");
						}
						else if(j!=0 && j!=n-1){
							bw.write(A[i][j]  + Gaps(kena));
						}	
					}else{
						if(j==0) {
							bw.write("          |"+A[i][j]+ Gaps(kena));
						}
						else if(j == n-1) {
							int finKena = gaps[j] -  A[i][j].length();
							bw.write(A[i][j] + Gaps(finKena)+"|");
						}
						else if(j!=0 && j!=n-1){
							bw.write(A[i][j]  + Gaps(kena));
						}	
					}
				}
				int kenaEq = 2 - Equin[i].length();
				bw.write("  |"+Equin[i]  + Gaps(kenaEq) + "|");


				int kenaB = maxSizeb - b[i].length();
				bw.write("  |"+b[i]  + Gaps(kenaB) + "|");

				bw.write("\n");
			}

			bw.write("\n");
			bw.write("                x>=0");

			bw.close();

			System.out.println("Ok with the creation...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static String Gaps(int epanalipseis) {
		String space = "";
		for(int i = 0;i<epanalipseis;i++) space+=" ";

		return space;
	}
	public static void CheckAndCreate() {

		if(!Checking()) {
			System.out.println("Ok with restrictions...");
			loadArrays();
			createFileLP2();
		}
		else System.out.println("Problem with restrictions!");

	}






















}







