package cz.czechitas.selenium;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestyPrihlasovaniNaKurzy {

    WebDriver prohlizec;

    @BeforeEach
    public void setUp() {
        System.setProperty("webdriver.gecko.driver", "C:\\Java-Training\\Selenium\\geckodriver.exe");
        prohlizec = new FirefoxDriver();
        prohlizec.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }

    @Test
    @Order(1)
    public void rodicSExistujicimUctemJeSchopenSePrihlasit() {
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/prihlaseni");
        prihlasenirodiceKarla();
        WebDriverWait cekani = new WebDriverWait(prohlizec, 10);
        cekani.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(text(),'Přihlášen')]")));
    }

    @Test
    @Order(2)
    public void rodicMuzeVybratKurzAPrihlasitsedoAplikaceAZapsatDite() {
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/");
        vyberKurzJava();
        prihlasenirodiceKarla();
        vyplnPrihlasku("Kája", "Gott", "27.2.1990");
        WebElement prihlasky = prohlizec.findElement(By.xpath("//a[contains(text(),'Přihlášky')]"));
        prihlasky.click();
        WebDriverWait cekani = new WebDriverWait(prohlizec, 10);
        cekani.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(text(),'Kája Gott')]")));

    }

    @Test
    @Order(3)
    public void rodicSeMuzePrihlasitVyhledatKurzAPrihlasitDite() {
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/prihlaseni");
        prihlasenirodiceKarla();
        WebElement novaPrihlaska = prohlizec.findElement(By.xpath("//a[contains(text(),'Vytvořit novou přihlášku')]"));
        novaPrihlaska.click();
        vyberKurzJava();
        vyplnPrihlasku("Kája2", "Gott", "27.2.1992");
        WebElement prihlasky = prohlizec.findElement(By.xpath("//a[contains(text(),'Přihlášky')]"));
        prihlasky.click();
        WebDriverWait cekani = new WebDriverWait(prohlizec, 10);
        cekani.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(text(),'Kája2 Gott')]")));
    }

    @Test
    @Order(5)
    public void rodicSeMuzeOdhlasitZUzivatelskehoProfilu() {
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/prihlaseni");
        prihlasenirodiceKarla();
        odhlaseniUzivatele();
        WebDriverWait cekani = new WebDriverWait(prohlizec, 10);
        cekani.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='https://cz-test-jedna.herokuapp.com/prihlaseni']")));
    }

    @Test
    @Order(4)
    public void rodicMuzeOdhlasitZakaZeVsechKurzuBezUdaniDuvodu() {
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/prihlaseni");
        prihlasenirodiceKarla();
        odhlasZakaZeVsechKurzu();
    }

    @AfterEach
    public void tearDown() {
        prohlizec.quit();
    }

    public void prihlasenirodiceKarla() {
        WebElement email = prohlizec.findElement(By.id("email"));
        email.sendKeys("karel.gott@gmail.com");
        WebElement heslo = prohlizec.findElement(By.id("password"));
        heslo.sendKeys("jsemKarelGott1");
        WebElement prihlasit = prohlizec.findElement(By.xpath("//button[contains(text(),'Přihlásit')]"));
        prihlasit.click();
    }

    public void vyberKurzJava() {
        List<WebElement> seznamTlacitekViceInfoVsechKurzu = prohlizec.findElements(By.xpath
                ("//div[@class = 'card']//a[text() = 'Více informací']"));
        WebElement tretiTlacitkoViceInformaci = seznamTlacitekViceInfoVsechKurzu.get(2);
        tretiTlacitkoViceInformaci.click();
        WebElement vytvoritPrihlasku = prohlizec.findElement(By.linkText("Vytvořit přihlášku"));
        vytvoritPrihlasku.click();
    }

    public void vyplnPrihlasku(String krestniZaka, String prijmeniZaka, String narozeniZaka) {
        WebElement termin = prohlizec.findElement(By.xpath("//div[contains(text(),'Vyberte termín')]"));
        termin.click();
        WebElement vyplnTermin = prohlizec.findElement(By.xpath("//input[@type='search']"));
        vyplnTermin.sendKeys("3");
        vyplnTermin.sendKeys("\n");
        WebElement krestniJmeno = prohlizec.findElement(By.id("forename"));
        krestniJmeno.sendKeys(krestniZaka);
        WebElement prijmeni = prohlizec.findElement(By.id("surname"));
        prijmeni.sendKeys(prijmeniZaka);
        WebElement narozeni = prohlizec.findElement(By.id("birthday"));
        narozeni.sendKeys(narozeniZaka);
        WebElement druhPlatby = prohlizec.findElement(By.xpath("//label[@for='payment_transfer']"));
        druhPlatby.click();
        WebElement podminkySouhlas = prohlizec.findElement(By.xpath("//label[@for='terms_conditions']"));
        podminkySouhlas.click();
        WebElement vytvorPrihlasku = prohlizec.findElement(By.xpath("//input[@value='Vytvořit přihlášku']"));
        vytvorPrihlasku.click();
    }

    public void odhlaseniUzivatele() {
        WebElement jmenoUzivatele = prohlizec.findElement(By.xpath("//a[@class='dropdown-toggle']"));
        jmenoUzivatele.click();
        WebElement odhlasit = prohlizec.findElement(By.id("logout-link"));
        odhlasit.click();
    }

    public void odhlasZakaZeVsechKurzu() {
        int pocetOdhlaseniZKurzu = 0;
        while (prohlizec.findElements(By.xpath("//a[@class='btn btn-sm btn-danger']")).size()>0) {
            WebElement odhlasovaciTlacitko = prohlizec.findElement(By.xpath("//a[@class='btn btn-sm btn-danger']"));
            odhlasovaciTlacitko.click();
            WebElement jinyDuvod = prohlizec.findElement(By.xpath("//label[@for='logged_out_other']"));
            jinyDuvod.click();
            WebElement odhlasitZaka = prohlizec.findElement(By.xpath("//input[@value='Odhlásit žáka']"));
            odhlasitZaka.click();
            pocetOdhlaseniZKurzu++;
        }
        Assertions.assertNotEquals(0, pocetOdhlaseniZKurzu, "Zak nemel zadny kurz na odhlaseni");
    }

}
