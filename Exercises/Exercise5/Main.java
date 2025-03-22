import java.util.ArrayList;
import java.util.List;

// Singleton Pattern
class WeatherStation {
    private static WeatherStation instance;
    private List<WeatherObserver> observers;
    private String weatherData;

    private WeatherStation() {
        observers = new ArrayList<>();
    }

    public static WeatherStation getInstance() {
        if (instance == null) {
            instance = new WeatherStation();
        }
        return instance;
    }

    public void addObserver(WeatherObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(WeatherObserver observer) {
        observers.remove(observer);
    }

    public void setWeatherData(String weatherData) {
        this.weatherData = weatherData;
        notifyObservers();
    }

    public String getWeatherData() {
        return weatherData;
    }

    private void notifyObservers() {
        for (WeatherObserver observer : observers) {
            observer.update(weatherData);
        }
    }
}

// Observer Pattern
interface WeatherObserver {
    void update(String weatherData);
}

class PhoneDisplay implements WeatherObserver {
    @Override
    public void update(String weatherData) {
        System.out.println("Phone Display: Weather data updated to " + weatherData);
    }
}

class TVDisplay implements WeatherObserver {
    @Override
    public void update(String weatherData) {
        System.out.println("TV Display: Weather data updated to " + weatherData);
    }
}

// Factory Pattern
abstract class WeatherSensor {
    public abstract String getWeatherData();
}

class TemperatureSensor extends WeatherSensor {
    @Override
    public String getWeatherData() {
        return "Temperature: 25°C";
    }
}

class HumiditySensor extends WeatherSensor {
    @Override
    public String getWeatherData() {
        return "Humidity: 60%";
    }
}

class WeatherFactory {
    public static WeatherSensor createSensor(String type) {
        switch (type) {
            case "Temperature":
                return new TemperatureSensor();
            case "Humidity":
                return new HumiditySensor();
            default:
                throw new IllegalArgumentException("Unknown sensor type");
        }
    }
}

// Main class to demonstrate the design patterns
public class Main {
    public static void main(String[] args) {
        // Singleton Pattern
        WeatherStation weatherStation = WeatherStation.getInstance();

        // Observer Pattern
        PhoneDisplay phoneDisplay = new PhoneDisplay();
        TVDisplay tvDisplay = new TVDisplay();
        weatherStation.addObserver(phoneDisplay);
        weatherStation.addObserver(tvDisplay);

        // Factory Pattern
        WeatherSensor temperatureSensor = WeatherFactory.createSensor("Temperature");
        WeatherSensor humiditySensor = WeatherFactory.createSensor("Humidity");

        // Simulate weather data updates
        weatherStation.setWeatherData(temperatureSensor.getWeatherData());
        weatherStation.setWeatherData(humiditySensor.getWeatherData());
    }
}