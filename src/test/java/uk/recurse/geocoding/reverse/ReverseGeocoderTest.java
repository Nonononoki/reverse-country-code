package uk.recurse.geocoding.reverse;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class ReverseGeocoderTest {

	private ReverseGeocoder geocoder;

	@BeforeAll
	void setup() {
		geocoder = new ReverseGeocoder();
	}

	public void reverseGeocoding() {

		InputStream in = ReverseGeocoderTest.class.getResourceAsStream("/baselineCities.txt");
		final List<Object[]> cities = new BufferedReader(new InputStreamReader(in, UTF_8)).lines()
				.map(line -> line.split("\t")).map(row -> {
					float lat = Float.parseFloat(row[0]);
					float lon = Float.parseFloat(row[1]);
					return new Object[] { lat, lon, row[2] };
				}).collect(Collectors.toList());

		for (Object[] o : cities) {
			float lat = (float) o[0];
			float lon = (float) o[1];
			String expectedIso = (String) o[2];
			String actualIso = geocoder.getCountry(lat, lon).map(Country::iso).orElse("null");
			assertEquals(actualIso, expectedIso, "lat=" + lat + " lon=" + lon);
		}
	}

	@Test
	public void streaming() {
		assertEquals(geocoder.countries().count(), 247);
	}
}
