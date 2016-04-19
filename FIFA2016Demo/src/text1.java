import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;
public class text1 {
	boolean main_mark=false;
	int Gnum = 20;
	int Inum = 20;
	static final Semaphore mutex = new Semaphore(1);
	static final Semaphore mutex2 = new Semaphore(5);
	static final Semaphore case1 = new Semaphore(0);
	static final Semaphore case2 = new Semaphore(0);
	static final Semaphore case3 = new Semaphore(0);
	int counterG = 0;
	int counterI = 0;
	int counter_tram = 0;
	public class German{
		int id;//German id
		German(int id){
			this.id=id;
		}
		public String toString(){
			return "German:"+id;
		}
	}
	
	public class Italian{
		int id;//German id
		Italian(int id){
			this.id=id;
		}
		public String toString(){
			return "Italian:"+id;
		}
	}
	
	public class station{
		Queue<German> queueG = new LinkedList<German>();
		Queue<Italian> queueI = new LinkedList<Italian>();
		
		//push method
		public synchronized void pushG(German x){
			if(main_mark==true){
				try{
	                wait(); //被按pause button
	            }catch (Exception e){
	                e.printStackTrace();
	            }
			}
			queueG.offer(x);
			System.out.println("German player "+(x.id+1)+" has come.");
		}
		public synchronized void pushI(Italian y){
			if(main_mark==true){
				try{
	                wait(); //被按pause button
	            }catch (Exception e){
	                e.printStackTrace();
	            }
			}
			queueI.offer(y);
			System.out.println("Italian player "+(y.id+1)+" has come.");
		}
		public synchronized German popG(){
			if(queueG.peek()!=null){
				System.out.println("German player "+(queueG.element().id+1)+" gets on the tram.");
				return queueG.poll();
			}else{
				System.out.println("Wrong with queueG");
				return null;
			}
			
		}
		public synchronized Italian popI(){
			if(queueI.peek()!=null){
				System.out.println("Italian player "+(queueI.element().id+1)+" gets on the tram.");
				return queueI.poll();
			}else{
				System.out.println("Wrong with queueI");
				return null;
			}
		}
		//resume function
		public synchronized void restart(){
			try{
                notifyAll(); //被按Resume button
            }catch (Exception e){
                e.printStackTrace();
            }
		}
	}
	public class tramcase1 implements Runnable{
		station ss=null;
		public tramcase1(station ss) {
			// TODO Auto-generated constructor stub
			this.ss = ss;
		}
		public void run(){
			while(true){
				try{
					case1.acquire();
					mutex.acquire();
					ss.popG();ss.popG();ss.popG();ss.popG();
					counter_tram +=1;
					System.out.println("The "+counter_tram + " round tram done.");
					mutex.release();
					mutex2.release();mutex2.release();mutex2.release();mutex2.release();
				}catch (InterruptedException e) {  
                    e.printStackTrace();  
                }
			}
		}
	}
	public class tramcase2 implements Runnable{
		station ss=null;
		public tramcase2(station ss) {
			// TODO Auto-generated constructor stub
			this.ss = ss;
		}
		public void run(){
			while(true){
				try{
					case2.acquire();
					mutex.acquire();
					ss.popI();ss.popI();ss.popI();ss.popI();
					counter_tram +=1;
					System.out.println("The "+counter_tram + " round tram done.");
					mutex.release();
					mutex2.release();mutex2.release();mutex2.release();mutex2.release();
				}catch (InterruptedException e) {  
                    e.printStackTrace();  
                }
			}
		}
	}
	public class tramcase3 implements Runnable{
		station ss=null;
		public tramcase3(station ss) {
			// TODO Auto-generated constructor stub
			this.ss = ss;
		}
		public void run(){
			while(true){
				try{
					case3.acquire();
					mutex.acquire();
					ss.popG();ss.popG();ss.popI();ss.popI();
					counter_tram +=1;
					System.out.println("The "+counter_tram + " round tram done.");
					mutex.release();
					mutex2.release();mutex2.release();mutex2.release();mutex2.release();
				}catch (InterruptedException e) {  
                    e.printStackTrace();  
                }
			}
		}
	}
	public class GermanHere implements Runnable{
		station ss=null;
		GermanHere(station ss){
			this.ss = ss;
		}
		public void run(){
			for(int i=0;i<Gnum;i++){
				try{
					Thread.sleep(1800);
					mutex2.acquire();
					mutex.acquire();
					German person = new German(i);
					ss.pushG(person);
					counterG +=1;
					if(counterG>=2){
						if(counterG==4){
							case1.release();//wake up case1
							counterG=0;
						}else if(counterI==2){
							case3.release();//wake up case3
							counterI-=2;
							counterG=0;
						}else if(counterI==3){
							case3.release();//wake up case3
							counterI-=2;
							counterG=0;
							mutex2.release();
						}
					}
					mutex.release();
				}catch (InterruptedException e) {  
                    e.printStackTrace();  
                } 
			}
		}
	}
	
	public class ItalianHere implements Runnable{
		station ss=null;
		ItalianHere(station ss){
			this.ss = ss;
		}
		public void run(){
			for(int i=0;i<Inum;i++){
				try{
					Thread.sleep(1500);
					mutex2.acquire();
					mutex.acquire();
					Italian person = new Italian(i);
					ss.pushI(person);
					counterI +=1;
					if(counterI>=2){
						if(counterI==4){
							case2.release();//wake up case2
							counterI=0;
						}else if(counterG==2){
							case3.release();//wake up case3
							counterG-=2;
							counterI=0;
						}else if(counterG==3){
							case3.release();//wake up case3
							counterG-=2;
							counterI=0;
							mutex2.release();
						}
					}
					mutex.release();
				}catch (InterruptedException e) {  
                    e.printStackTrace();  
                } 
			}
		}
	}
	//resume thread
	public class resume implements Runnable{
		station ss=null;
		resume(station ss){
			this.ss = ss;
		}
		public void run(){
			while(true){
				try{Thread.sleep(1);
				if(main_mark==false)
					ss.restart();
				}catch (InterruptedException e) {  
                    e.printStackTrace();  
                }
			}
		}
	}
	
	public class evt implements ActionListener{
		public void actionPerformed(ActionEvent arg0){
			station s = new station();
			GermanHere ger=new GermanHere(s);
			ItalianHere ita=new ItalianHere(s);
			tramcase1 t1 = new tramcase1(s);
			tramcase2 t2 = new tramcase2(s);
			tramcase3 t3 = new tramcase3(s);
			resume r = new resume(s);//resume
			Thread tt1 = new Thread(t1);
			Thread tt2 = new Thread(t2);
			Thread tt3 = new Thread(t3);
			Thread tg = new Thread(ger);
			Thread ti = new Thread(ita);
			Thread tr = new Thread(r);//resume
			tt1.start();tt2.start();tt3.start();
			tg.start();
			ti.start();
			tr.start();//resume
		}
	}
	public evt construct(){
		return new evt();
	}
	


}

