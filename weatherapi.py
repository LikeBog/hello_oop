import requests
import os
from dotenv import load_dotenv
from datetime import datetime

# Load API key from .env file
load_dotenv()
API_KEY = os.getenv("OWMAPIKEY")

BASE_URL = "https://api.openweathermap.org/data/2.5/weather"

def get_weather_data(city, units="metric"):
    params = {
        "q": city,
        "appid": API_KEY,
        "units": units
    }
    try:
        response = requests.get(BASE_URL, params=params)
        response.raise_for_status()
        return response.json()
    except requests.RequestException as e:
        print("Error connecting to OpenWeatherMap:", e)
        return None

def format_sun_time(timestamp, timezone):
    if timestamp is None:
        return "No data"
    dt = datetime.utcfromtimestamp(timestamp + timezone)
    return dt.strftime('%H:%M:%S')

def display_weather(data, units):
    try:
        city = data.get("name", "Unknown")
        country = data.get("sys", {}).get("country", "Unknown")
        temp = data.get("main", {}).get("temp", "No data")
        humidity = data.get("main", {}).get("humidity", "No data")
        wind_speed = data.get("wind", {}).get("speed", "No data")
        weather_desc = data.get("weather", [{}])[0].get("description", "No data")
        sunrise = format_sun_time(data.get("sys", {}).get("sunrise"), data.get("timezone", 0))
        sunset = format_sun_time(data.get("sys", {}).get("sunset"), data.get("timezone", 0))

        unit_str = "Celsius" if units == "metric" else "Fahrenheit"
        print(f"\nWeather for {city}, {country}:")
        print(f"  Description: {weather_desc.capitalize()}")
        print(f"  Temperature: {temp}° ({unit_str})")
        print(f"  Humidity: {humidity}%")
        print(f"  Wind Speed: {wind_speed} m/s")
        print(f"  Sunrise: {sunrise}")
        print(f"  Sunset: {sunset}\n")
    except Exception as e:
        print("Could not parse weather details:", e)

def main():
    if not API_KEY:
        print("No API key found. Please check your .env file.")
        return
    print("Welcome to the Console Weather App!\n")
    while True:
        city = input("Enter a city name (or 'exit' to quit): ").strip()
        if city.lower() == "exit":
            print("Goodbye!")
            break
        units_input = input("Choose units: (C)elsius or (F)ahrenheit? [C/F]: ").strip().lower()
        units = "metric" if units_input == "c" else "imperial"
        data = get_weather_data(city, units)
        if data:
            if data.get("cod") == 200:
                display_weather(data, units)
            else:
                print("Error:", data.get("message", "Unknown error."))
        else:
            print("Could not retrieve weather information.\n")

if __name__ == "__main__":
    main()
