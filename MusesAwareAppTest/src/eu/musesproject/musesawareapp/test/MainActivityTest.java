///*
// * version 1.0 - MUSES prototype software
// * Copyright MUSES project (European Commission FP7) - 2013 
// * 
// */
//package eu.musesproject.musesawareapp.test;
//
//import android.test.ActivityInstrumentationTestCase2;
//
//import com.jayway.android.robotium.solo.Solo;
//
//import eu.musesproject.musesawareapp.ui.MainActivity;
//
///**
// * MainActivityTest class performs robotium tests on Main GUi
// * 
// * @author Yasir Ali
// * @version Jan 27, 2014
// */
//
//public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {
//
//	private Solo solo;
//	private int NUMBER_OF_ITERATIONS=1;
//	private int SLEEP_INTERVAL=2000;
//	
//	public MainActivityTest() {
//	    super(MainActivity.class);
//	}
//
//	@Override
//	protected void setUp() throws Exception {
//	    solo = new Solo(getInstrumentation(), getActivity());
//	}
//
//	@Override
//	protected void tearDown() throws Exception {
//	    solo.finishOpenedActivities();
//	
//	}
//	
//	/**
//	 * User opens an asset A and receives positive feedback
//	 * @throws Exception
//	 */
//	 
//	
//	public void testOpenAssetA() throws Exception{
//		while(NUMBER_OF_ITERATIONS>0){
//			solo.assertCurrentActivity("wrong activiy", MainActivity.class);
//		    solo.clickOnButton(solo.getString(eu.musesproject.musesawareapp.R.string.asset_a));
//	    	boolean actual = solo.waitForText("Action .. accepted", 
//	    									   1, 3000, true);
//	    	assertEquals("Open asset ..", true, actual);
//	    	solo.sleep(SLEEP_INTERVAL);
//	    	NUMBER_OF_ITERATIONS--;
//		}
//	}
//	
//	/**
//	 * User opens an asset B and receives negative feedback
//	 * @throws Exception
//	 */
//	 
//	
//	public void testOpenAssetB() throws Exception{
//		while(NUMBER_OF_ITERATIONS>0){
//			solo.assertCurrentActivity("wrong activiy", MainActivity.class);
//		    solo.clickOnButton(solo.getString(eu.musesproject.musesawareapp.R.string.asset_b));
//	    	boolean actual = solo.waitForText("Action .. denied", 
//					   1, 3000, true);
//	    	assertEquals("Open asset ..", true, actual);
//	    	solo.sleep(SLEEP_INTERVAL);
//	    	NUMBER_OF_ITERATIONS--;
//		}
//	}
//	
//	
//}
