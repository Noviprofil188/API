const axios = require('axios');
const xml2js = require('xml2js');

// Dohvati ime grada iz argumenta naredbenog retka
const cityName = process.argv[2];

if (!cityName) {
    console.error('Molimo upisite ime grada.');
    process.exit(1);
}

// URL za dohvat XML podataka
const url = 'https://vrijeme.hr/hrvatska_n.xml';

// Dohvati XML podatke
axios.get(url)
    .then(response => {
        // Parsiraj XML podatke
        xml2js.parseString(response.data, (err, result) => {
            if (err) {
                console.error('Greška prilikom parsiranja podataka:', err);
                process.exit(1);
            }

            // Pronađi podatke za traženi grad
            const cities = result.Hrvatska.Grad;
            const city = cities.find(c => c.GradIme[0] === cityName);

            if (!city) {
                console.error('Grad pronađen.');
                process.exit(1);
            }

            // Kreiraj objekt s podacima o vremenu
            const weatherData = {
                city: city.GradIme[0],
                latitude: city.Lat[0],
                longitude: city.Lon[0],
                temperature: city.Podatci[0].Temp[0],
                humidity: city.Podatci[0].Vlaga[0],
                pressure: city.Podatci[0].Tlak[0],
                pressureTrend: city.Podatci[0].TlakTend[0],
                windDirection: city.Podatci[0].VjetarSmjer[0],
                windSpeed: city.Podatci[0].VjetarBrzina[0],
                weather: city.Podatci[0].Vrijeme[0],
                weatherSign: city.Podatci[0].VrijemeZnak[0]
            };

            // Ispiši podatke o vremenu
            console.log(JSON.stringify(weatherData, null, 2));
        });
    })
    .catch(error => {
        console.error('Greška pri dohvatu podataka:', error);
        process.exit(1);
    });