package org.cvg.junit5_app.ejemplo.models;

import org.cvg.junit5_app.ejemplo.exceptions.InsuficientMoneyException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class CuentaTest {
    Cuenta cuenta;

    @BeforeEach
    public void init() {
        System.out.println("Iniciando método test");
        this.cuenta = new Cuenta("CRISTHIAN VILLEGAS", new BigDecimal("1000.12345"));
    }

    /**
     * PRUEBAS PARAMETRIZADAS
     */
    @Nested
    @DisplayName("Pruebas parametrizadas")
    @Tag("param")  // Etiquetar test, permite determinar si se ejecutan todos o algunos
    class ParametricTestClass {
        @DisplayName("Test para validar la funcion debito de la cuenta")
        @ParameterizedTest(name = "Iteracion {index} ejecutando con valor {0}")
        @ValueSource(strings = {"100", "200", "300", "500", "700", "900"})
        void testDebitoCuenta(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertAll(
                    () -> assertNotNull(cuenta.getSaldo(), () -> "El saldo no debe ser nulo"),
                    () -> assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0, "El valor esperado no es igual al recibido")
                    //() -> assertEquals("900.12345", cuenta.getSaldo().toPlainString(), () -> "El valor esperado no es igual al recibido")
            );
        }

        @DisplayName("Test para validar la funcion debito de la cuenta")
        @ParameterizedTest(name = "Iteracion {index} ejecutando con valor {0}")
        @CsvSource({"1,100", "2,200", "3,300", "4,500", "5,700", "6,1000"})
        void testDebitoCuentaCsv(String index, String monto) {
            System.out.println("Evaluando " + index + " -> " + monto);
            cuenta.debito(new BigDecimal(monto));
            assertAll(
                    () -> assertNotNull(cuenta.getSaldo(), () -> "El saldo no debe ser nulo"),
                    () -> assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0, "El valor esperado no es igual al recibido")
                    //() -> assertEquals("900.12345", cuenta.getSaldo().toPlainString(), () -> "El valor esperado no es igual al recibido")
            );
        }

        @DisplayName("Test para validar la funcion debito de la cuenta")
        @ParameterizedTest(name = "Iteracion {index} ejecutando con valor {0}")
        @CsvSource({"150,100", "300,200", "400,300", "5001,500", "701,700", "1200,1000"})
        void testDebitoCuentaCsv2(String saldo, String monto) {
            System.out.println("Evaluando " + saldo + " -> " + monto);
            cuenta.setSaldo(new BigDecimal(saldo));
            cuenta.debito(new BigDecimal(monto));
            assertAll(
                    () -> assertNotNull(cuenta.getSaldo(), () -> "El saldo no debe ser nulo"),
                    () -> assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0, "El valor esperado no es igual al recibido")
                    //() -> assertEquals("900.12345", cuenta.getSaldo().toPlainString(), () -> "El valor esperado no es igual al recibido")
            );
        }

        @DisplayName("Test para validar la funcion debito de la cuenta")
        @ParameterizedTest(name = "Iteracion {index} ejecutando con valor {0}")
        @CsvFileSource(resources = "/data.csv")
        void testDebitoCuentaCsvFile(String monto) {
            cuenta.debito(new BigDecimal(monto));
            assertAll(
                    () -> assertNotNull(cuenta.getSaldo(), () -> "El saldo no debe ser nulo"),
                    () -> assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0, "El valor esperado no es igual al recibido")
                    //() -> assertEquals("900.12345", cuenta.getSaldo().toPlainString(), () -> "El valor esperado no es igual al recibido")
            );
        }
    }

    @DisplayName("Test para validar la funcion debito de la cuenta")
    @ParameterizedTest(name = "Iteracion {index} ejecutando con valor {0}")
    @MethodSource("montoList")
    void testDebitoCuentaMethodSource(String monto) {
        cuenta.debito(new BigDecimal(monto));
        assertAll(
                () -> assertNotNull(cuenta.getSaldo(), () -> "El saldo no debe ser nulo"),
                () -> assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0, "El valor esperado no es igual al recibido")
                //() -> assertEquals("900.12345", cuenta.getSaldo().toPlainString(), () -> "El valor esperado no es igual al recibido")
        );
    }

    static List<String> montoList() {
        return Arrays.asList("100", "200", "300", "500", "700", "1000");
    }

    @Nested
    @DisplayName("Nested class for bank account test")
    @Tag("cuenta")
    class BankAccountTest {
        @DisplayName("Test para validar el nombre del propietario de la cuenta")
        @Test
        void testNombreCuenta() {
            //cuenta.setPersona("CRISTHIAN VILLEGAS");
            String valorEsperado = "CRISTHIAN VILLEGAS";
            String valorReal = cuenta.getPersona();
            assertAll(
                    () -> assertNotNull(valorReal, () -> "El valor no puede ser nulo"),
                    () -> assertEquals(valorEsperado, valorReal, () -> "El valor esperado no es igual al valor real"),
                    () -> assertTrue(valorReal.equals(valorEsperado), () -> "Los valores son diferentes...")
            );
        }

        @DisplayName("Test para comprobar el saldo de la cuenta")
        @Test
        void testSaldoCuenta() {
            assertAll(
                    () -> assertEquals(1000.12345, cuenta.getSaldo().doubleValue(), () -> "El saldo no es igual al esperado"),
                    () -> assertFalse(cuenta.getSaldo().compareTo( BigDecimal.ZERO ) < 0, () -> "El saldo no debe ser menor a 0"),
                    () -> assertTrue(cuenta.getSaldo().compareTo( BigDecimal.ZERO ) > 0, () -> "El saldo debe ser mayor a 0"),
                    () -> assertNotNull(cuenta.getSaldo(), () -> "El saldo no debe ser nulo")
            );
        }

        /**
         * TDD
         */
        @DisplayName("Test para validar si las cuentas son iguales")
        @Test
        void testReferenciaCuenta() {
            Cuenta cuenta = new Cuenta("Jhon Doe", new BigDecimal("8900.9997"));
            Cuenta cuenta2 = new Cuenta("Jhon Doe", new BigDecimal("8900.9997"));

//        assertNotEquals(cuenta, cuenta2);
            assertEquals(cuenta, cuenta2, () -> "Las cuentas no son iguales");
        }

        @DisplayName("Test para validar la funcion debito de la cuenta")
        @Test
        void testDebitoCuenta() {
            Cuenta cuenta = new Cuenta("CRISTHIAN", new BigDecimal("1000.12345"));
            cuenta.debito(new BigDecimal(100));
            assertAll(
                    () -> assertNotNull(cuenta.getSaldo(), () -> "El saldo no debe ser nulo"),
                    () -> assertEquals(900, cuenta.getSaldo().intValue(), "El valor esperado no es igual al recibido"),
                    () -> assertEquals("900.12345", cuenta.getSaldo().toPlainString(), () -> "El valor esperado no es igual al recibido")
            );
        }

        @DisplayName("Test para validar la funcion credito de la cuenta")
        @Test
        void testCreditoCuenta() {
            Cuenta cuenta = new Cuenta("CRISTHIAN", new BigDecimal("1000.12345"));
            cuenta.credito(new BigDecimal(100));
            assertAll(
                    () -> assertNotNull(cuenta.getSaldo(), () -> "El saldo no debe ser null"),
                    () -> assertEquals(1100, cuenta.getSaldo().intValue(), () -> "El valor recibido es diferente al esperado"),
                    () -> assertEquals("1100.12345", cuenta.getSaldo().toPlainString(), () -> "El valor recibido es diferente al esperado")
            );
        }


        @DisplayName("Test para devolver una excepcion en caso de dinero insuficiente...")
        @Test
        @Tag("error")
        void testDineroInsuficienteException() {
            Cuenta cuenta = new Cuenta("CRISTHIAN", new BigDecimal("1000.12345"));
            Exception exception = assertThrows(InsuficientMoneyException.class, () -> {
                cuenta.debito( new BigDecimal(1500) );
            });
            String actual = exception.getMessage();
            String esperado = "Dinero insuficiente";
            assertEquals(esperado, actual);
        }
    }

    @Nested
    @DisplayName("Nested class for Operating systems test")
    class OperatingSystemTest {
        /**
         * VALIDATION TEST
         */
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {

        }
        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinux() {

        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void noWindows() {
        }
    }

    @Nested
    class JavaVersionTest {
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void soloJdk8() {
        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void enableOnJava15() {
        }

        @Test
        void imprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach( (key, val) -> System.out.println(key + ": " + val) );
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = "1.8.0_111")
        void testJavaVersion() {
        }

    }


    @Nested
    class SystemPropertiesTest {
        @DisplayName("Ejecutar solo en entornos de 64 bits")
        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void disableOn32Bits() {
        }

        @DisplayName("Ejecutar solo en entornos de 32 bits")
        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
        void disableOn64Bits() {
        }

        @DisplayName("Ejecutar solo en sesiones de ciertos usuarios")
        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "crist")
        void testUsername() {
        }

        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev() {
        }
    }


    @Nested
    class EnvironmentVariables {
        @Test
        void imprimirVariablesAmbiente() {
            Map<String, String> map = System.getenv();
            map.forEach((k, v) -> System.out.println(k + ": " + v));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-17.0.2")
        void testJavaHome() {
        }

        @Test
        @DisplayName("Test para pc con 12 nucleos de procesamiento")
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "12")
        void testProcessors() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv() {
        }
        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
        void testDisabledEnv() {
        }
    }


    @Nested
    @DisplayName("Nested class for assumptions tests")
    class AssumptionsTests {
        /**
         * ASUMPTIONS ejecucion de pruebas con base en suposiciones
         */

        @DisplayName("Se ejecuta solo en ambientes de desarrollo")
        @Test
        void testSaldoCuentaDev() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            assumeTrue(esDev); // Si no se cumple se deshabilita el test
            assertAll(
                    () -> assertEquals(1000.12345, cuenta.getSaldo().doubleValue(), () -> "El saldo no es igual al esperado"),
                    () -> assertFalse(cuenta.getSaldo().compareTo( BigDecimal.ZERO ) < 0, () -> "El saldo no debe ser menor a 0"),
                    () -> assertTrue(cuenta.getSaldo().compareTo( BigDecimal.ZERO ) > 0, () -> "El saldo debe ser mayor a 0"),
                    () -> assertNotNull(cuenta.getSaldo(), () -> "El saldo no debe ser nulo")
            );
        }

        @DisplayName("Se ejecuta solo en ambientes de desarrollo")
        @Test
        void testSaldoCuentaDev2() {
            boolean esDev = "dev".equals(System.getProperty("ENV"));
            assumingThat(esDev, () -> { // Si no se cumple se deshabilita el test
                assertAll(
                        () -> assertEquals(1000.12345, cuenta.getSaldo().doubleValue(), () -> "El saldo no es igual al esperado"),
                        () -> assertFalse(cuenta.getSaldo().compareTo( BigDecimal.ZERO ) < 0, () -> "El saldo no debe ser menor a 0"),
                        () -> assertTrue(cuenta.getSaldo().compareTo( BigDecimal.ZERO ) > 0, () -> "El saldo debe ser mayor a 0"),
                        () -> assertNotNull(cuenta.getSaldo(), () -> "El saldo no debe ser nulo")
                );
            });

        }
    }

    /**
     * TIME OUT para determinar si un test falla o no despues de un tiempo
     */
    @Nested
    @Tag("timeout")
    class TimeOutTest {
        @Test
        @Timeout(1)
        void pruebaTimeOut() throws InterruptedException {
            TimeUnit.SECONDS.sleep(1);
        }

        @Test
        @Timeout(value = 1000, unit = TimeUnit.MILLISECONDS)
        void pruebaTimeOut2() throws InterruptedException {
            TimeUnit.MILLISECONDS.sleep(900);
        }

        @Test
        void testTimeOutAssert() {
            assertTimeout(Duration.ofSeconds(5), () -> {
                TimeUnit.MILLISECONDS.sleep(4000);
            });
        }
    }
}