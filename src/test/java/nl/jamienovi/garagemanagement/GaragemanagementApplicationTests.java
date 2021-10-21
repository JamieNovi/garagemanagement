package nl.jamienovi.garagemanagement;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@SpringBootTest
class GaragemanagementApplicationTests {

	Calculate underTest = new Calculate();

	@Test
	void shouldAddTwoNumbers() {
		//Arrange
		int numberOne = 30;
		int numberTwo = 20;

		//Act
		int result = underTest.add(numberOne,numberTwo);

		//Assert
		int expected = 50;
		assertThat(result).isEqualTo(expected);
	}

	class Calculate {
		int add(int a, int b){ return a + b;}
	}

}
