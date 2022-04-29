package dakrory.a7med.cargomarine;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.wifi.ScanResult;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import dakrory.a7med.cargomarine.ThetaHandler.API.API;
import dakrory.a7med.cargomarine.ThetaHandler.API.MJpegInputStream;
import dakrory.a7med.cargomarine.ThetaHandler.API.RequestModels.session_name;
import dakrory.a7med.cargomarine.ThetaHandler.API.RequestModels.startSession;
import dakrory.a7med.cargomarine.ThetaHandler.API.ResponseModels.SET_API_VERSION_Response;
import dakrory.a7med.cargomarine.ThetaHandler.API.ResponseModels.data_Response;
import dakrory.a7med.cargomarine.ThetaHandler.API.ResponseModels.startSessionResponse;
import dakrory.a7med.cargomarine.ThetaHandler.API.ResponseModels.takePicture_Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.thanosfisherman.wifiutils.WifiUtils;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionErrorCode;
import com.thanosfisherman.wifiutils.wifiConnect.ConnectionSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CameraActivity extends Activity{


	String BASE_URL = "http://192.168.1.1:80";

	ImageButton capture;

	ImageButton connectToWifi;
	String urlImage="";
	String urlImage_last = "";
	String sessionId=null;
	ImageView imgView;
	ImageButton startLive;

	ProgressDialog progress ;
	TextView notifyHint;

	API api;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int SDK_INT = Build.VERSION.SDK_INT;
		if (SDK_INT > 8)
		{
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
			//your codes here

		}
		setContentView(R.layout.activity_main_camera);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		if(!WifiUtils.withContext(getApplicationContext()).isWifiConnected()) {
			WifiUtils.withContext(getApplicationContext()).enableWifi(this::checkResult);
		}
		progress = new ProgressDialog(this);
		progress.setTitle("Loading");
		progress.setMessage("Wait while loading...");
		progress.setCancelable(false); // disable dismiss by tapping outside of the dialog

		capture = (ImageButton)findViewById(R.id.capture);
		startLive = (ImageButton)findViewById(R.id.startLive);

		capture.setActivated(false);
		imgView=(ImageView)findViewById(R.id.imgView);
		connectToWifi = (ImageButton) findViewById(R.id.connectToWifi);
		notifyHint = (TextView)findViewById(R.id.notifyHint);
		notifyHint.setText(getResources().getString(R.string.HintForOpenWifi));




		startLive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				initializeTheCameraAndLivePreview();
//				startLive.setVisibility(View.GONE);
			}
		});
		//The gson builder
		Gson gson = new GsonBuilder()
				.setLenient()
				.create();


		//creating retrofit object
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(BASE_URL)
				.addConverterFactory(GsonConverterFactory.create(gson))
				.build();

		//creating our api
		api = retrofit.create(API.class);

		connectToWifi.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				handle_Wifi_process();

			}
		});




		capture.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Log.v("AhmedDakrory:","Start Upload3");
				//creating a call and calling the upload image method

					capture_now(sessionId, api);



			}
		});













	}



	private void handle_Wifi_process() {
		progress.setMessage("Search for wifi....");
		progress.show();
		WifiUtils.withContext(getApplicationContext()).scanWifi(this::getScanResults).start();
	}


	private void getScanResults(@NonNull final List<ScanResult> results)
	{


		if (results.isEmpty())
		{
			Log.v("TAG", "SCAN RESULTS IT'S EMPTY");
			Toast.makeText(CameraActivity.this, "WIFI Issue, We will restart the wifi", Toast.LENGTH_SHORT).show();
			progress.dismiss();
			return;
		}


		AlertDialog.Builder builderSingle = new AlertDialog.Builder(CameraActivity.this);
		builderSingle.setIcon(R.mipmap.theta);
		builderSingle.setTitle("Select Theta Wifi:-");

		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(CameraActivity.this, android.R.layout.select_dialog_singlechoice);
		for(int i=0;i<results.size();i++){
			arrayAdapter.add(results.get(i).SSID);
		}

		builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String strName = arrayAdapter.getItem(which);
				AlertDialog.Builder builderInner = new AlertDialog.Builder(CameraActivity.this);
				final EditText edittext = new EditText(CameraActivity.this);
				builderInner.setMessage(strName);
				builderInner.setTitle("Enter the Password");

				builderInner.setView(edittext);
				builderInner.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,int which) {
						dialog.dismiss();
						progress.setMessage("Connecting....");
						progress.show();



						WifiUtils.withContext(getApplicationContext())
								.connectWith(strName, edittext.getText().toString())
								.setTimeout(40000)
								.onConnectionResult(new ConnectionSuccessListener() {
									@Override
									public void success() {
										Toast.makeText(CameraActivity.this, "SUCCESS!", Toast.LENGTH_SHORT).show();
										progress.dismiss();
										notifyHint.setText("Connected");

										initializeTheCameraAndLivePreview();
									}

									@Override
									public void failed(@NonNull ConnectionErrorCode errorCode) {
										Toast.makeText(CameraActivity.this, "EPIC FAIL!" , Toast.LENGTH_SHORT).show();
										progress.dismiss();
									}
								})
								.start();







					}
				});
				builderInner.show();
			}
		});

		builderSingle.show();
		progress.dismiss();

//		Log.v("TAG", "GOT SCAN RESULTS " + results);
	}

	private void initializeTheCameraAndLivePreview() {
		progress.setMessage("Initializing Camera");
		progress.show();

			Call<startSessionResponse> call = api.StartSession(new startSession("camera.startSession",null));
			call.enqueue(new Callback<startSessionResponse>() {
				@Override
				public void onResponse(Call<startSessionResponse> call, Response<startSessionResponse> response) {

					try {
						progress.setMessage("Set Versioning for Camera.....");
						progress.show();
						setAPI_VERSION(response.body().results.sessionId, api);
					}catch (Error err){
						Log.v("AhmedDakrory","Error: Camera,  "+err.toString());
						progress.dismiss();
						livePrev(api);
					}catch (Exception exc){
						Log.v("AhmedDakrory","Error: Camera,  "+exc.toString());
						progress.dismiss();
						livePrev(api);
					}

				}

				@Override
				public void onFailure(Call<startSessionResponse> call, Throwable t) {
					Log.v("AhmedDakrory",t.toString());
					Toast.makeText(CameraActivity.this,"Error while starting the camera (Start Session)",Toast.LENGTH_LONG).show();
					progress.dismiss();
				}
			});






	}



    private void checkResult(boolean isSuccess)
    {
        if (isSuccess)
            Toast.makeText(CameraActivity.this, "WIFI ENABLED", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(CameraActivity.this, "COULDN'T ENABLE WIFI", Toast.LENGTH_SHORT).show();
    }

	private void livePrev( API api) {
		capture.setActivated(true);
		Thread th1=new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(BASE_URL + "/osc/commands/execute");
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("POST");
					connection.setDoInput(true);
					connection.setDoOutput(true);

					JSONObject input = new JSONObject();
					input.put("name", "camera.getLivePreview");

					OutputStream os = connection.getOutputStream();
					os.write(input.toString().getBytes());

					connection.connect();
					os.flush();
					os.close();

					InputStream is = connection.getInputStream();
					MJpegInputStream mjis = new MJpegInputStream(is);
					final Date[] d1 = {new Date()};
					final Date[] d2 = {new Date()};
					boolean keepRunning = true;
					while (keepRunning) {
						try {
							String data = mjis.readMJpegFrame();

							Bitmap bitmap = mjis.convert(data);

                            Log.v("AhmedDakrory", String.valueOf(bitmap.getHeight()));
							runOnUiThread(new Runnable() {
								@Override
								public void run() {

									imgView.setImageBitmap(bitmap);

								}
							});

						} catch (IOException e11) {
							keepRunning = false;
							Log.v("AhmedDakrory", String.valueOf("Error Read Live, "+e11.toString()));

						}
					}

				} catch (IOException | JSONException e) {
					e.printStackTrace();
				}


			}
		});
		th1.start();



	}



	private void setAPI_VERSION(String sessionId, API api) {
		JsonObject param = new JsonObject();
		param.addProperty("sessionId",sessionId);
		JsonObject clientVersion=new JsonObject();
		clientVersion.addProperty("clientVersion",2);
		param.add("options",clientVersion);

		Call<SET_API_VERSION_Response> call = api.SET_API_VERSION(new startSession("camera.setOptions",param));
		call.enqueue(new Callback<SET_API_VERSION_Response>() {
			@Override
			public void onResponse(Call<SET_API_VERSION_Response> call, Response<SET_API_VERSION_Response> response) {
				CameraActivity.this.sessionId = sessionId;
				try {
					progress.setMessage("Set Parameters for Camera.....");
					progress.show();
					setParameters(sessionId, api);
				}catch (Error err){
					Log.v("AhmedDakrory","Error:  Camera,  "+err.toString());
					progress.dismiss();
					livePrev(api);
				}

			}

			@Override
			public void onFailure(Call<SET_API_VERSION_Response> call, Throwable t) {
				Toast.makeText(CameraActivity.this,"Error while starting the camera (Set Parameters)",Toast.LENGTH_LONG).show();
				progress.dismiss();
			}
		});
	}

	private void setParameters(String sessionId, API api) {

		JsonObject param = new JsonObject();

		JsonObject fileFormat=new JsonObject();
		fileFormat.addProperty("type","jpeg");
		fileFormat.addProperty("width","2048");
		fileFormat.addProperty("height","1024");

		JsonObject options=new JsonObject();
		options.add("fileFormat",fileFormat);

		param.add("options",options);

		Log.v("AhmedDakrory",param.toString());
		Call<SET_API_VERSION_Response> call = api.setSettings(new startSession("camera.setOptions",param));
		call.enqueue(new Callback<SET_API_VERSION_Response>() {
			@Override
			public void onResponse(Call<SET_API_VERSION_Response> call, Response<SET_API_VERSION_Response> response) {
				progress.dismiss();
				livePrev(api);

			}

			@Override
			public void onFailure(Call<SET_API_VERSION_Response> call, Throwable t) {
				Toast.makeText(CameraActivity.this,"Error while starting the camera (Set Options)",Toast.LENGTH_LONG).show();
				progress.dismiss();
			}
		});

	}

	private void capture_now(String sessionId, API api) {
		progress.setMessage("Load Image....");
		progress.show();
		startLive.setVisibility(View.VISIBLE);
		Call<takePicture_Response> call = api.takePicture(new session_name("camera.takePicture"));
		call.enqueue(new Callback<takePicture_Response>() {
			@Override
			public void onResponse(Call<takePicture_Response> call, Response<takePicture_Response> response) {

				 retrieve_image_Last(sessionId, api);
			}

			@Override
			public void onFailure(Call<takePicture_Response> call, Throwable t) {

			}
		});
	}

	private void retrieve_image(String sessionId, API api) {

		Call<data_Response> call =api.handleData();

		call.enqueue(new Callback<data_Response>() {
			@Override
			public void onResponse(Call<data_Response> call, Response<data_Response> response) {
				urlImage = response.body().state._latestFileUrl;
				if(urlImage_last==urlImage){
					retrieve_image(sessionId,api);
					progress.setMessage("Handle Image");
					progress.show();
				}else{
					progress.dismiss();
					OpenTheImage(urlImage);
				}

				Log.v("AhmedDakrory",response.body().fingerprint);
			}

			@Override
			public void onFailure(Call<data_Response> call, Throwable t) {
				Log.v("AhmedDakroryError2",t.toString());

			}
		});


	}

	private void OpenTheImage(String urlImage) {

		capture.setActivated(true);
		AlertDialog.Builder builderInner = new AlertDialog.Builder(CameraActivity.this);
//		final EditText edittext = new EditText(MainActivity.this);
//		builderInner.setMessage(strName);

		builderInner.setIcon(R.mipmap.cap);
		builderInner.setTitle("Do you want to save this image to Repo?");
		builderInner.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				Log.v("AhmedDakrorySa","Saving");
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						initializeTheCameraAndLivePreview();
					}
				});
			}
		});



		builderInner.setPositiveButton("Save", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {

				new DownloadFileFromURL().execute(urlImage);
			}
		});

		builderInner.show();
//					Intent i = new Intent(MainActivity.this, PanoramaView.class);
//					i.putExtra("pan3D", urlImage);
//					startActivity(i);

	}

	private void retrieve_image_Last(String sessionId, API api) {


		Call<data_Response> call = api.handleData();
		call.enqueue(new Callback<data_Response>() {
			@Override
			public void onResponse(Call<data_Response> call, Response<data_Response> response) {
				urlImage_last = response.body().state._latestFileUrl;
				retrieve_image(sessionId,api);
				Log.v("AhmedDakrory",response.body().fingerprint);
			}

			@Override
			public void onFailure(Call<data_Response> call, Throwable t) {
				Log.v("AhmedDakroryError2",t.toString());

			}
		});


	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

	}

	@Override
	protected void onResume() {
		super.onResume();

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED||
					checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED||
					checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED||
					checkSelfPermission(Manifest.permission.ACCESS_WIFI_STATE) != PackageManager.PERMISSION_GRANTED||
					checkSelfPermission(Manifest.permission.CHANGE_WIFI_STATE) != PackageManager.PERMISSION_GRANTED||
					checkSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED)
			{
				requestPermissions(new String[]{

						Manifest.permission.ACCESS_COARSE_LOCATION,
						Manifest.permission.ACCESS_FINE_LOCATION,
						Manifest.permission.ACCESS_WIFI_STATE,
						Manifest.permission.CHANGE_WIFI_STATE,
						Manifest.permission.WRITE_EXTERNAL_STORAGE,
						Manifest.permission.ACCESS_NETWORK_STATE

				}, 87);
			}
		}



		initializeTheCameraAndLivePreview();

	}

	@Override
	protected void onPause() {

		super.onPause();
	}




	class DownloadFileFromURL extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread
		 * Show Progress Bar Dialog
		 * */

		dialogWithProgress mainDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//			showDialog(progress_bar_type);
			mainDialog=new dialogWithProgress(CameraActivity.this);
			mainDialog.dialog.show();
			mainDialog.setPercentage(0);
		}

		/**
		 * Downloading file in background thread
		 * */



		@Override
		protected String doInBackground(String... f_url) {


							int count;
							File folder = new File(Environment.getExternalStorageDirectory() + File.separator +"PrimeShippingCarServices");
							boolean success = true;
							if (!folder.exists()) {
								//Toast.makeText(MainActivity.this, "Directory Does Not Exist, Create It", Toast.LENGTH_SHORT).show();
								success = folder.mkdir();
							}

							try {
								if (success) {

									Log.v("AhmedDakrory",Environment.getExternalStorageDirectory().toString());
									URL url = new URL(f_url[0]);

									HttpURLConnection connection = (HttpURLConnection) url.openConnection();

//									connection.setDoOutput(true);
									connection.connect();
									int lenghtOfFile = connection.getContentLength();
//									lenghtOfFile = lenghtOfFile*




									// this will be useful so that you can show a tipical 0-100%
									// progress bar

									Log.v("AhmedDakroryPTotal",String.valueOf(lenghtOfFile));
									// download the file
									InputStream input  = connection.getInputStream();//new BufferedInputStream(url.openStream(),8192);


									//Toast.makeText(MainActivity.this, "Directory Created", Toast.LENGTH_SHORT).show();

									String Imagename = urlImage.split("/")[urlImage.split("/").length-1];
									// Output stream to write file
									OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().toString() +File.separator +"PrimeShippingCarServices"+File.separator +Imagename);

									byte data[] = new byte[1024];

									long total = 0;
									int percetange = 0;
									while ((count = input.read(data)) != -1) {
										total += count;
										// publishing the progress....
										// After this onProgressUpdate will be called
										percetange = (int)((total*100 / lenghtOfFile));
										publishProgress( ""+percetange);

										Log.v("AhmedDakroryPer",String.valueOf(count+","+lenghtOfFile+","+total+","+percetange));
										// writing data to file
										output.write(data, 0, count);
									}

									// flushing output
									output.flush();

									// closing streams
									output.close();
									input.close();

								}


							} catch (Exception e) {
								Log.v("AhmedDakrory","Error: "+ e.getMessage());
							}

			return null;


		}
		/**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(String... progress) {
			// setting progress percentage
			mainDialog.dialog.show();
			Log.v("AhmedDakrory",String.valueOf((progress[0])));
			mainDialog.setPercentage(Integer.parseInt(progress[0]));
		}

		/**
		 * After completing background task
		 * Dismiss the progress dialog
		 * **/
		@Override
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after the file was downloaded
			mainDialog.dialog.dismiss();
//			dismissDialog(progress_bar_type);
//
//			// Displaying downloaded image into image view
//			// Reading image path from sdcard
//			String imagePath = Environment.getExternalStorageDirectory().toString() + "/downloadedfile.jpg";
//			// setting downloaded into image view
//			my_image.setImageDrawable(Drawable.createFromPath(imagePath));
		}

	}







	class dialogWithProgress extends Dialog{

		final Dialog dialog ;
		ProgressBar text;
		TextView text2;
		public dialogWithProgress(@NonNull Context context) {
			super(context);
			dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCancelable(false);
			dialog.setContentView(R.layout.dialog_progress);
			text = (ProgressBar) dialog.findViewById(R.id.progress_horizontal);
			text2 = dialog.findViewById(R.id.value123);






			Window window = dialog.getWindow();
			window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);


		}


		public Dialog getDialog() {
			return dialog;
		}

		public void setPercentage(int percentage){
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					text.setProgress(percentage);
					text2.setText(String.valueOf(percentage));
				}
			});




		}
	}

}