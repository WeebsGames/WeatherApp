import javafx.animation.*;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import weather.Period;
import weather.WeatherAPI;
import javafx.geometry.Point2D;
//import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;


public class JavaFX extends Application {
	Group startRender, sideInfo, settingsRender, forecastRender;
	Text temperature, weather, location, date, mouseX, mouseY,
			todayTemp, day1temp, day2temp, day3temp, day4temp, day5temp, day6temp,
			todayDesc, day1desc, day2desc, day3desc, day4desc, day5desc, day6desc,
			sevenDayTitle, de_bug, settingsTitle, tempScale, distScale,
			windSpd, detailedForecast, precipitation, precip;
	VBox tempweath, datelocation, mousePos, forecastVBox, sideVbox, precipVbox, cityVbox;
	HBox todayHBox, day1HBox, day2HBox, day3HBox, day4HBox, day5HBox, day6HBox, sideButtons;
	Circle tempcirc, datecirc, settingsTitlecirc;
	Color blue, grayBlue;
	StackPane tempStack, dateStack, settingsTitleStack, windSpdStack, detailedForecastStack,
			precipitationStack, cityStack;
	Rectangle grass, sideBar, sideBox, settingsBox, forecastBox, weatherIconBox,
			windSpdBox, detailedForecastBox, precipitationBox, cityBox, cloudBox, tempBox;
	Polygon sideArrow;
	TranslateTransition sideTransition, backTransition, rainTransition;
	RotateTransition gearTransition;
	ScaleTransition breathingTransition, hoverlargeTransition;
	Button changeCity, sevenDayButton, settings, settingsBack, changeTemp, forecastBack,
			chicago, newYork, sanFrancisco;
	ToggleButton debugSwitch;
	ImageView cityIconView, cityOnIconView, gearIconView, backIconView, weatherBaseView, calendarIconView,
			weatherIconView, todayIconView, rainIconView,
			day1IconView, day2IconView, day3IconView, day4IconView, day5IconView, day6IconView;
	ArrayList<Period> forecast, nycForecast, sfForecast;
	String[] officeList = {"LOT", "OKX", "MTR"};
	int[][] coords = {{77, 70}, {34, 37},{83, 108}};
	int year, day, select = 0;
	String month;
	boolean debug, sideBoxVisible, celsuis = false;
	double mX, mY;

	public static void main(String[] args) {
		System.out.println(Font.getFontNames());
		launch(args);
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("I'm a professional Weather App!");
		//int temp = WeatherAPI.getTodaysTemperature(77,70);
		forecast = new ArrayList<>(WeatherAPI.getForecast(officeList[0], coords[0][0], coords[0][1]));
		nycForecast = new ArrayList<>(WeatherAPI.getForecast(officeList[1], coords[1][0], coords[1][1]));
		sfForecast = new ArrayList<>(WeatherAPI.getForecast(officeList[2], coords[2][0], coords[2][1]));
		if (forecast == null || nycForecast == null) {
			throw new RuntimeException("Forecast did not load");
		}
		//variable initialization
		temperature = new Text();
		weather = new Text();
		location = new Text();
		date = new Text();
		blue = Color.rgb(0, 117, 206);
		grayBlue = Color.rgb(138, 187, 255);
		grass = new Rectangle(400, 10, Color.GREEN);
		sideBar = new Rectangle(50, 100, grayBlue);
		settingsBox = new Rectangle(50, 800, grayBlue);
		forecastBox = new Rectangle(50, 800, grayBlue);
		cloudBox = new Rectangle(400, 800, Color.GRAY);
		sideArrow = new Polygon();
		mouseX = new Text();
		mouseY = new Text();
		sevenDayTitle = new Text();
		todayTemp = new Text();
		day1temp = new Text();
		day2temp = new Text();
		day3temp = new Text();
		day4temp = new Text();
		day5temp = new Text();
		day6temp = new Text();
		todayDesc = new Text();
		day1desc = new Text();
		day2desc = new Text();
		day3desc = new Text();
		day4desc = new Text();
		day5desc = new Text();
		day6desc = new Text();
		todayIconView = new ImageView();
		day1IconView = new ImageView();
		day2IconView = new ImageView();
		day3IconView = new ImageView();
		day4IconView = new ImageView();
		day5IconView = new ImageView();
		day6IconView = new ImageView();
		changeCity = new Button();
		sevenDayButton = new Button();
		settings = new Button();
		settingsBack = new Button();
		forecastBack = new Button();

		weatherBaseView = loadImage("weather_base.png");
		weatherBaseView.setFitHeight(250);
		weatherBaseView.setFitWidth(250);
		setLayout(weatherBaseView, 0, 540);

		//debug
		mousePos = new VBox(mouseX, mouseY);

		//decoration
		grass.setLayoutY(790);
		rainIconView = loadImage("rain.png");
		rainIconView.setScaleX(1.25);
		rainIconView.setScaleY(3.75);
		rainIconView.setTranslateX(0);
		rainIconView.setLayoutY(-4800);
		rainTransition = new TranslateTransition(Duration.seconds(5), rainIconView);
		rainTransition.setCycleCount(Animation.INDEFINITE);
		rainTransition.setByX(-2000);
		rainTransition.setByY(8000);
		rainTransition.setInterpolator(Interpolator.LINEAR);
		rainTransition.play();

		//sidebar
		setLayout(sideBar, 375, 350);
		sideBar.setArcHeight(30);
		sideBar.setArcWidth(30);

		sideArrow.getPoints().addAll(new Double[]{
				380.0, 400.0,
				390.0, 380.0,
				390.0, 420.0
				}
		);
		sideArrow.setFill(grayBlue.darker());

		//sidebar icon
		weatherIconView = loadImage(forecast.get(0).icon);
		weatherIconBox = new Rectangle(105, 105, blue);
		setLayout(weatherIconView, 425, 135);
		setLayout(weatherIconBox, 415, 125);
		weatherIconBox.setArcHeight(10);
		weatherIconBox.setArcWidth(10);

		//precipitation
		precipitation = new Text(String.valueOf(forecast.get(0).probabilityOfPrecipitation.value) + "%");
		precip = new Text("Chance of Precipitation");
		precip.setWrappingWidth(80);
		precip.setTextAlignment(TextAlignment.CENTER);
		precipitation.setFont(new Font("Lucida Sans Unicode", 20));
		precip.setFont(new Font("Lucida Sans Unicode", 10));
		precipVbox = new VBox(precipitation, precip);
		precipVbox.setAlignment(Pos.CENTER);
		precipitationBox = new Rectangle(105, 105, blue);
		precipitationBox.setArcHeight(10);
		precipitationBox.setArcWidth(10);
		precipitationStack = new StackPane(
				precipitationBox,
				precipVbox
		);
		setLayout(precipitationStack, 620, 125);
		rainIconView.setOpacity(forecast.get(0).probabilityOfPrecipitation.value/100.0);
		cloudBox.setOpacity(forecast.get(0).probabilityOfPrecipitation.value/100.0);

		//wind speed
		windSpd = new Text("Wind Speed: " + forecast.get(0).windSpeed);
		windSpd.setFont(new Font("Lucida Sans Unicode", 20));
		windSpdBox = new Rectangle(310, 30, blue);
		windSpdBox.setArcHeight(10);
		windSpdBox.setArcWidth(10);
		windSpdStack = new StackPane(
				windSpdBox, windSpd
		);
		setLayout(windSpdStack, 415, 245);

		//detailed forecast
		detailedForecast = new Text(forecast.get(0).detailedForecast);
		detailedForecast.setWrappingWidth(290);
		detailedForecast.setFont(new Font("Lucida Sans Unicode", 17));
		detailedForecastBox = new Rectangle(310, 200+detailedForecast.getText().length()/3, blue);
		detailedForecastBox.setArcHeight(10);
		detailedForecastBox.setArcWidth(10);
		detailedForecastStack = new StackPane(
				detailedForecastBox, detailedForecast
		);
		setLayout(detailedForecastStack, 415, 300);
		detailedForecast.setTranslateX(-10);
		detailedForecastStack.setAlignment(Pos.TOP_RIGHT);

		//seven-day forecast
		sevenDayTitle.setText("Seven Day Forecast");
		sevenDayTitle.setFont(new Font("Lucida Sans Unicode", 21));
		sevenDayTitle.setTextAlignment(TextAlignment.CENTER);
		setLayout(sevenDayTitle, 100, 50);
		initializeForecast(todayDesc, todayIconView, todayTemp, forecast, 0);
		initializeForecast(day1desc, day1IconView, day1temp, forecast, 2);
		initializeForecast(day2desc, day2IconView, day2temp, forecast, 4);
		initializeForecast(day3desc, day3IconView, day3temp, forecast, 6);
		initializeForecast(day4desc, day4IconView, day4temp, forecast, 8);
		initializeForecast(day5desc, day5IconView, day5temp, forecast, 10);
		initializeForecast(day6desc, day6IconView, day6temp, forecast, 12);
		todayHBox = new HBox(10, todayDesc, todayIconView, todayTemp);
		todayHBox.setAlignment(Pos.CENTER);
		day1HBox = new HBox(10, day1desc, day1IconView, day1temp);
		day1HBox.setAlignment(Pos.CENTER);
		day2HBox = new HBox(10, day2desc, day2IconView, day2temp);
		day2HBox.setAlignment(Pos.CENTER);
		day3HBox = new HBox(10, day3desc, day3IconView, day3temp);
		day3HBox.setAlignment(Pos.CENTER);
		day4HBox = new HBox(10, day4desc, day4IconView, day4temp);
		day4HBox.setAlignment(Pos.CENTER);
		day5HBox = new HBox(10, day5desc, day5IconView, day5temp);
		day5HBox.setAlignment(Pos.CENTER);
		day6HBox = new HBox(10, day6desc, day6IconView, day6temp);
		day6HBox.setAlignment(Pos.CENTER);

		forecastVBox = new VBox(10, todayHBox, day1HBox, day2HBox, day3HBox, day4HBox, day5HBox, day6HBox);
		setLayout(forecastVBox, 60, 120);

		backIconView = loadImage("back.png");
		forecastBack.setGraphic(backIconView);
		forecastBack.setTranslateY(5);
		forecastBack.getStyleClass().add("back");
		backIconView.setFitWidth(50);
		backIconView.setFitHeight(20);

		//buttons at the bottom of the sidebar
		sideButtons = new HBox(10, changeCity, sevenDayButton, settings);
		setLayout(sideButtons, 440, 535);

		cityIconView = loadImage("city.png");
		cityOnIconView = loadImage("city_on.png");
		cityIconView.setFitWidth(70);
		cityIconView.setFitHeight(70);
		cityOnIconView.setFitHeight(70);
		cityOnIconView.setFitWidth(70);
		changeCity.setGraphic(cityIconView);
		changeCity.getStyleClass().add("default");
		changeCity.setOnMouseEntered(e -> {
			changeCity.setGraphic(cityOnIconView);
		});
		changeCity.setOnMouseExited(e -> {
			changeCity.setGraphic(cityIconView);
		});

		chicago = new Button();
		chicago.setText("Chicago");
		chicago.getStyleClass().add("default");
		newYork = new Button();
		newYork.setText("New York");
		newYork.getStyleClass().add("default");
		sanFrancisco = new Button();
		sanFrancisco.setText("San Francisco");
		sanFrancisco.getStyleClass().add("default");
		cityVbox = new VBox(10, chicago, newYork, sanFrancisco);
		cityVbox.setAlignment(Pos.CENTER);
		cityBox = new Rectangle(200, 250, blue.darker());
		cityBox.setArcHeight(30);
		cityBox.setArcWidth(30);
		cityBox.setOpacity(0.9);
		cityStack = new StackPane(
				cityBox, cityVbox
		);
		cityStack.setAlignment(Pos.CENTER);
		setLayout(cityStack, 100, 200);
		cityStack.setVisible(false);

		sevenDayButton.getStyleClass().add("default");
		calendarIconView = loadImage("calendar.png");
		calendarIconView.setFitWidth(70);
		calendarIconView.setFitHeight(70);
		sevenDayButton.setGraphic(calendarIconView);
		sevenDayButton.getStyleClass().add("default");

		gearIconView = loadImage("gear.png");
		gearIconView.setFitWidth(70);
		gearIconView.setFitHeight(70);
		settings.setGraphic(gearIconView);
		settings.getStyleClass().add("default");

		//enlarge when hover
		hoverlargeTransition = new ScaleTransition(Duration.seconds(0.1), calendarIconView);
		hoverlargeTransition.setToX(0.9);
		hoverlargeTransition.setToY(0.9);
		hoverlargeTransition.play();
		sevenDayButton.setOnMouseEntered(e->{
			hoverlargeTransition.setToX(1.0);
			hoverlargeTransition.setToY(1.0);
			hoverlargeTransition.play();
		});
		sevenDayButton.setOnMouseExited(e->{
			hoverlargeTransition.setToX(0.9);
			hoverlargeTransition.setToY(0.9);
			hoverlargeTransition.play();
		});

		//rotate settings button
		gearTransition = new RotateTransition(Duration.seconds(0.5), gearIconView);
		gearTransition.setFromAngle(0);
		gearTransition.setToAngle(360);
		settings.setOnMouseEntered(event-> {
			gearTransition.play();
		});

		//grouping all sidebar stuff
		sideBox = new Rectangle(400, 510+detailedForecast.getText().length()/3, grayBlue);
		setLayout(sideBox, 400, 110);
		sideBox.setArcHeight(30);
		sideBox.setArcWidth(30);
		sideVbox = new VBox(
				detailedForecastStack,
				sideButtons
		);
		setLayout(sideVbox, 415, 300);
		sideVbox.setAlignment(Pos.CENTER);
		sideButtons.setTranslateY(10);
		sideButtons.setTranslateX(15);
		sideInfo = new Group(
				sideBar,
				sideBox,
				sideArrow,
				weatherIconBox,
				weatherIconView,
				windSpdStack,
				precipitationStack,
				sideVbox
		);
		sideTransition = new TranslateTransition(Duration.seconds(0.1), sideInfo);

		//temp and short desc
		temperature.setText(" "+String.valueOf(forecast.get(0).temperature) + "°");
		temperature.setFont(new Font("Lucida Sans Unicode", 45));
		weather.setText(forecast.get(0).shortForecast);
		weather.setFont(new Font("Lucida Sans Unicode", 15));
		weather.setWrappingWidth(150);
		weather.setTextAlignment(TextAlignment.CENTER);
		location.setText("Chicago");

		tempcirc = encircle(temperature, blue.brighter(), 30+(forecast.get(0).shortForecast).length()/2);
		tempcirc.setOpacity(0.7);
		tempBox = ensquare(temperature, blue.brighter(), 30+(forecast.get(0).shortForecast).length()/2);
		breathingTransition = new ScaleTransition(Duration.seconds(2.5), tempcirc);
		breathingTransition.setToX(0.9);
		breathingTransition.setToY(0.9);
		breathingTransition.setCycleCount(Animation.INDEFINITE);
		breathingTransition.setAutoReverse(true);
		breathingTransition.play();
		tempweath = new VBox(temperature, weather);
		tempweath.setAlignment(Pos.CENTER);

		tempStack = new StackPane();
		tempStack.getChildren().addAll(tempcirc,tempweath);
		tempStack.setAlignment(Pos.CENTER);
		setLayout(tempStack, 200-tempcirc.getRadius(), 175);


		//date and month
		year = java.time.LocalDate.now().getYear();
		month = java.time.LocalDate.now().getMonth().toString();
		month = month.charAt(0) + month.substring(1).toLowerCase();
		day = java.time.LocalDate.now().getDayOfMonth();
		date.setText(month + " " + day);
		date.setFont(new Font("Lucida Sans Unicode", 20));

		datecirc = encircle(date, Color.TRANSPARENT, 10);
		datelocation = new VBox(5, date, location);
		datelocation.setAlignment(Pos.CENTER);

		dateStack = new StackPane();
		dateStack.getChildren().addAll(datecirc, datelocation);
		dateStack.setLayoutX(200 - datecirc.getRadius());

		//back button for settings menu
		backIconView = loadImage("back.png");
		backIconView.setFitWidth(50);
		backIconView.setFitHeight(20);
		settingsBack.setGraphic(backIconView);
		settingsBack.setTranslateY(5);
		settingsBack.getStyleClass().add("back");
		backTransition = new TranslateTransition(Duration.seconds(0.2), backIconView);
		backTransition.setCycleCount(4);
		backTransition.setToX(-10);
		backTransition.setAutoReverse(true);
		settingsBack.setOnMouseEntered(e-> {
			backTransition.play();
		});

		//settings menu
		settingsTitle = new Text("Settings");
		settingsTitlecirc = encircle(settingsTitle, Color.TRANSPARENT, 10);
		settingsTitle.setFont(new Font("Lucida Sans Unicode", 30));
		settingsTitleStack = new StackPane();
		settingsTitleStack.getChildren().addAll(settingsTitle, settingsTitlecirc);
		setLayout(settingsTitleStack, 200 - settingsTitlecirc.getRadius(), 30);

		//debug swtich
		de_bug = new Text("Debug");
		de_bug.setFont(new Font("Lucida Sans Unicode", 15));
		setLayout(de_bug, 100, 145);
		debugSwitch = new ToggleButton();
		debugSwitch.setMinSize(30,30);
		setLayout(debugSwitch, 300, 125);
		debugSwitch.getStyleClass().add("toggle");
		if(debug) {
			debugSwitch.setSelected(false);
		}

		//temp scale switch
		tempScale = new Text("Degree scale: Fahrenheit");
		tempScale.setFont(new Font("Lucida Sans Unicode", 15));
		setLayout(tempScale, 100, 185);
		changeTemp = new Button("C");
		changeTemp.setFont(new Font("Lucida Sans Unicode", 15));
		changeTemp.getStyleClass().add("default");
		changeTemp.setMinSize(30, 30);
		setLayout(changeTemp, 300, 165);

		//rendering stuff
		startRender = new Group(
				cloudBox,
				rainIconView,
				tempStack,
				dateStack,
				weatherBaseView,
				grass,
				sideInfo,
				mousePos,
				cityStack
		);
		settingsRender = new Group(
				settingsBox,
				settingsBack,
				settingsTitleStack,
				de_bug,
				debugSwitch,
				tempScale,
				changeTemp
		);
		forecastRender = new Group(
				forecastVBox,
				forecastBox,
				sevenDayTitle,
				forecastBack
		);
		Scene startScreen = new Scene(startRender, 400,800);
		startScreen.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		Scene settingsScreen = new Scene(settingsRender,400, 800);
		settingsScreen.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		Scene forecastScreen = new Scene(forecastRender,400, 800);
		forecastScreen.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
		primaryStage.setScene(startScreen);
		primaryStage.setResizable(false);
		primaryStage.show();

		//code for mouse movement on startScreen
		startScreen.setOnMouseMoved(e ->{
			mX = e.getX();
			mY = e.getY();
			//debug for mouse position
			if(debug){
				mouseX.setText("Mouse X: " + String.valueOf(mX));
				mouseY.setText("Mouse Y: " + String.valueOf(mY));
			}
			//'buttonless' button code
			if(mX > 380 & mY > 354.5 & mY < 443.5 & !sideBoxVisible){
				if(debug){
					sideBar.setFill(Color.RED);
				}
				sideTransition.setToX(-350);
				sideTransition.play();
				sideArrow.setRotate(180);
				sideBoxVisible = true;
			} else {
				if (debug) {
					sideBar.setFill(grayBlue);
				}
			}
			if(mX > 25 & mX < 50 & mY > 354.5 & mY < 443.5 & sideBoxVisible){
				sideTransition.setToX(0);
				sideTransition.play();
				sideArrow.setRotate(0);
				sideBoxVisible = false;
				cityStack.setVisible(false);
			}
		});
		settingsScreen.setOnMouseMoved(e ->{
			mX = e.getX();
			mY = e.getY();
			//debug for mouse position
			if(debug){
				mouseX.setText("Mouse X: " + String.valueOf(mX));
				mouseY.setText("Mouse Y: " + String.valueOf(mY));
			}
		});

		//change scene buttons (used in sidebar)
		changeCity.setOnAction(e -> {
			System.out.println("change city");
			cityStack.setVisible(!cityStack.isVisible());
		});
		chicago.setOnAction(e->{
			System.out.println("chicago");
			location.setText("Chicago");
			cityChooser(forecast);
			cityStack.setVisible(false);
		});
		newYork.setOnAction(e->{
			System.out.println("newYork");
			location.setText("New York");
			cityChooser(nycForecast);
			cityStack.setVisible(false);
		});
		sanFrancisco.setOnAction(e->{
			System.out.println("sanFrancisco");
			location.setText("San Francisco");
			cityChooser(sfForecast);
			cityStack.setVisible(false);
		});

		sevenDayButton.setOnAction(e ->{
			System.out.println("forecast scene");
			mousePos.setTranslateY(20);
			if(debug){
				settingsRender.getChildren().remove(mousePos);
				startRender.getChildren().remove(mousePos);
				forecastRender.getChildren().add(mousePos);
			}
			primaryStage.setScene(forecastScreen);
		});
		forecastBack.setOnAction(e -> {
			System.out.println("forecast back");
			mousePos.setTranslateY(0);
			if(debug){
				settingsRender.getChildren().remove(mousePos);
				forecastRender.getChildren().remove(mousePos);
				startRender.getChildren().add(mousePos);
			}
			primaryStage.setScene(startScreen);
		});
		settings.setOnAction(e -> {
			System.out.println("settings");
			mousePos.setTranslateY(20);
			if(debug){
				startRender.getChildren().remove(mousePos);
				settingsRender.getChildren().add(mousePos);
			}
			primaryStage.setScene(settingsScreen);
		});
		settingsBack.setOnAction(e -> {
			System.out.println("settings back");
			mousePos.setTranslateY(0);
			if(debug){
				settingsRender.getChildren().remove(mousePos);
				forecastRender.getChildren().remove(mousePos);
				startRender.getChildren().add(mousePos);
			}
			primaryStage.setScene(startScreen);
		});
		debugSwitch.setOnAction(e -> {
			System.out.println("debug switch");
			debug = !debug;
			if(!debug){
				startRender.getChildren().remove(mousePos);
				settingsRender.getChildren().remove(mousePos);
				forecastRender.getChildren().remove(mousePos);
				primaryStage.setResizable(false);
			} else {
				settingsRender.getChildren().add(mousePos);
				primaryStage.setResizable(true);
			}
		});
		changeTemp.setOnAction(e -> {
			System.out.println("change temp");
			if(!celsuis){
				if(location.getText().equals("Chicago")){
					setCelsius(forecast);
				}
				if(location.getText().equals("New York")){
					setCelsius(nycForecast);
				}
				if(location.getText().equals("San Francisco")){
					setCelsius(sfForecast);
				}
			} else {
				if(location.getText().equals("Chicago")){
					setFahrenheit(forecast);
				}
				if(location.getText().equals("New York")){
					setFahrenheit(nycForecast);
				}
				if(location.getText().equals("San Francisco")){
					setFahrenheit(sfForecast);
				}
			}

			celsuis = !celsuis;
		});


	}

	private void cityChooser(ArrayList<Period> forecast) {
		weatherIconView.setImage(new Image(forecast.get(0).icon));
		precipitation.setText(String.valueOf(forecast.get(0).probabilityOfPrecipitation.value) + "%");
		rainIconView.setOpacity(forecast.get(0).probabilityOfPrecipitation.value/100.0);
		cloudBox.setOpacity(forecast.get(0).probabilityOfPrecipitation.value/100.0);
		windSpd.setText("Wind Speed: " + forecast.get(0).windSpeed);
		detailedForecast.setText(forecast.get(0).detailedForecast);
		detailedForecastBox.setHeight(200+ forecast.get(0).detailedForecast.length()/3);
		sideBox.setHeight(510+detailedForecast.getText().length()/3);
		initializeForecast(todayDesc, todayIconView, todayTemp, forecast, 0);
		initializeForecast(day1desc, day1IconView, day1temp, forecast, 2);
		initializeForecast(day2desc, day2IconView, day2temp, forecast, 4);
		initializeForecast(day3desc, day3IconView, day3temp, forecast, 6);
		initializeForecast(day4desc, day4IconView, day4temp, forecast, 8);
		initializeForecast(day5desc, day5IconView, day5temp, forecast, 10);
		initializeForecast(day6desc, day6IconView, day6temp, forecast, 12);
		temperature.setText(" "+String.valueOf(forecast.get(0).temperature) + "°");
		if(celsuis){
			temperature.setText(" "+String.valueOf(toCelsius(forecast.get(0).temperature)) + "°");
		}
		weather.setText(forecast.get(0).shortForecast);
		tempcirc.setRadius(80+(forecast.get(0).shortForecast).length()/2);
		tempStack.setLayoutX(200-tempcirc.getRadius());
	}

	//template example
	private Circle encircle(Text text, Color color, int padding){
		//returns a new circle that surrounds a text
		Circle circle = new Circle();
		circle.setFill(color);
		//set radius of circle based on text length
		circle.setRadius(getWidth(text) / 2 + padding);
		return circle;
	}

	private Rectangle ensquare(Text text, Color color, int padding){
		//returns a new rectangle that surrounds a text
		Rectangle rect = new Rectangle();
		rect.setFill(color);
		//set the height and width of the square based on text length
		rect.setWidth(getWidth(text) / 2 + padding);
		rect.setHeight(getWidth(text) / 2 + padding);
		return rect;
	}

	private double getWidth(Text text){
		//returns the width of text without converting the original text to css
		//apply css to get the width of text
		text.applyCss();
		return text.getLayoutBounds().getWidth();
	}


	//template method
	private void initializeForecast(Text date, ImageView icon, Text temp, ArrayList<Period> forecast, int period){
		//creates a hbox with the temperature and time of the period
		date.setText(forecast.get(period).name);
		temp.setText(String.valueOf(forecast.get(period).temperature)+"°");
		icon.setImage(new Image(forecast.get(period).icon));
		date.setFont(new Font("Lucida Sans Unicode", 20));
		temp.setFont(new Font("Lucida Sans Unicode", 20));
	}

	//adapter example
	private ImageView loadImage(String path){
		//loads path into an ImageView it returns
		Image img = new Image(path);
		//if the image doesnt load send an error and return a placeholder image
		if(img.isError()){
			System.out.println("Error: "+path+" did not load");
			return new ImageView("gear.png");
		}
        return new ImageView(img);
	}

	private void setLayout(Node node, double x, double y){
		node.setLayoutX(x);
		node.setLayoutY(y);
	}

	private int toCelsius(double fahrenheit){
		return (int) Math.round((fahrenheit-32)/1.8);
	}

	private void setCelsius(ArrayList<Period> forecast){
		temperature.setText(" "+toCelsius(forecast.getFirst().temperature) + "°");
		todayTemp.setText(String.valueOf(toCelsius(forecast.get(0).temperature))+"°");
		day1temp.setText(String.valueOf(toCelsius(forecast.get(2).temperature))+"°");
		day2temp.setText(String.valueOf(toCelsius(forecast.get(4).temperature))+"°");
		day3temp.setText(String.valueOf(toCelsius(forecast.get(6).temperature))+"°");
		day4temp.setText(String.valueOf(toCelsius(forecast.get(8).temperature))+"°");
		day5temp.setText(String.valueOf(toCelsius(forecast.get(10).temperature))+"°");
		day6temp.setText(String.valueOf(toCelsius(forecast.get(12).temperature))+"°");
		changeTemp.setText("F");
		tempScale.setText("Degree scale: Celsius");
	}

	private void setFahrenheit(ArrayList<Period> forecast){
		temperature.setText(" "+String.valueOf(forecast.getFirst().temperature) + "°");
		todayTemp.setText(String.valueOf(forecast.get(0).temperature)+"°");
		day1temp.setText(String.valueOf(forecast.get(2).temperature)+"°");
		day2temp.setText(String.valueOf(forecast.get(4).temperature)+"°");
		day3temp.setText(String.valueOf(forecast.get(6).temperature)+"°");
		day4temp.setText(String.valueOf(forecast.get(8).temperature)+"°");
		day5temp.setText(String.valueOf(forecast.get(10).temperature)+"°");
		day6temp.setText(String.valueOf(forecast.get(12).temperature)+"°");
		changeTemp.setText("C");
		tempScale.setText("Degree scale: Fahrenheit");
	}

}
