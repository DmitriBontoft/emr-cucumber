package stepdefs;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.xmlunit.assertj.XmlAssert.assertThat;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;
import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import helpers.XmlFileLoader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import jdk.nashorn.internal.runtime.regexp.RegExp;
import jdk.nashorn.internal.runtime.regexp.RegExpFactory;
import jdk.nashorn.internal.runtime.regexp.RegExpMatcher;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

public class ReasonForVisitDefinitions {
  private byte[] aesKey = "e1f3e22435686a2b".getBytes();
  private byte[] aesIv = "5bd9671e45d21856".getBytes();
  public static final String SITE_URL = "http://localhost:8089";
  private String responseString;
  private Node sectionNode;
  private CloseableHttpClient closeableHttpClient = HttpClients.createDefault();

  @Given("^User is logged in$")
  public void user_is_logged_in() throws Exception {
    // Write code here that turns the phrase above into concrete actions

  }

  @When("^User requests a Visit Summary for patient (\\d+) encounter (\\d+)$")
  public void i_request_a_CCD_for_patient_encounter(int arg1, int arg2) throws Exception {
    // Write code here that turns the phrase above into concrete actions
    String uri = String.format(SITE_URL +"/mobiledoc/interoperability/documents/ccd/visit-summary?patientId=%d&encounterId=%d&format=CCDCCR", arg1, arg2 );
    HttpGet request = new HttpGet(
        uri);
    request.addHeader("accept", "application/xml");
    request.addHeader("Cookie", "JSESSIONID=462552197CF6826643B4CC042EF08C8A");
    HttpResponse httpResponse = closeableHttpClient.execute(request);
    responseString = convertResponseToString(httpResponse);
  }

  private String convertResponseToString(HttpResponse httpResponse) throws IOException {
    InputStream responseStream = httpResponse.getEntity().getContent();
    Scanner scanner = new Scanner(responseStream, "UTF-8");
    String responseString = scanner.useDelimiter("\\Z").next();
    scanner.close();
    return responseString;
  }

  @Then("^The CCD element at \"([^\"]*)\" will contain the xml in \"([^\"]*)\"$")
  public void the_CCD_element_at_will_contain_the_xml_in(String arg1, String arg2) throws Exception {
    DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
    Document parseTest = f.newDocumentBuilder()
        .parse(new InputSource(new StringReader(responseString)));
    Document parseControl = f.newDocumentBuilder()
        .parse(new InputSource(new StringReader(XmlFileLoader.loadXmlFile(arg2))));
    XPathFactory xPathFactory = XPathFactory.newInstance();
    XPath xPath = xPathFactory.newXPath();
    XPathExpression compile = xPath
        .compile(arg1);
    Node test = (Node) compile.evaluate(parseTest, XPathConstants.NODE);
    XPathExpression compile2 = xPath
        .compile("/node()");
    Node control = (Node) compile2.evaluate(parseControl, XPathConstants.NODE);

    assertThat(test)
        .and(control)
        .ignoreWhitespace()
        .normalizeWhitespace()
        .areSimilar();
  }


  @Then("^A CCD will be generated where each (.*) evaluates to (.*)$")
  public void a_CCD_will_be_generated_where_each_evaluates_to(String arg1, String arg2, java.util.Map<String,String> arg3) throws Exception {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // List<YourType>, List<List<E>>, List<Map<K,V>> or Map<K,V>.
    // E,K,V must be a scalar (String, Integer, Date, enum etc)
    for(String key : arg3.keySet()) {
      String xpath = key;
      String value = arg3.get(xpath);
      DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
      Document parseTest = f.newDocumentBuilder()
          .parse(new InputSource(new StringReader(responseString)));
      XPathFactory xPathFactory = XPathFactory.newInstance();
      XPath xPath = xPathFactory.newXPath();
      XPathExpression compile = xPath.compile(xpath);
      String evaluate = compile.evaluate(parseTest);
      assertEquals(value, evaluate);
    }
  }

  @Then("^A CCD will be generated with a section at (.*)$")
  public void a_CCD_will_be_generated_with_a_section_coded(String sectionXpath) throws Exception {
    // Write code here that turns the phrase above into concrete actions
    DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
    Document parseTest = f.newDocumentBuilder()
        .parse(new InputSource(new StringReader(responseString)));
    XPathFactory xPathFactory = XPathFactory.newInstance();
    XPath xPath = xPathFactory.newXPath();
    XPathExpression compile = xPath.compile(sectionXpath);
    Node evaluate = (Node) compile.evaluate(sectionXpath, XPathConstants.NODE);
    assertNotNull(evaluate);
    this.sectionNode = evaluate;
  }

  @Then("^the section at (.*) will contain the following values$")
  public void the_section_will_contain_the_following_values(String sectionXpath, java.util.Map<String,String> arg1) throws Exception {
    // Write code here that turns the phrase above into concrete actions
    // For automatic transformation, change DataTable to one of
    // List<YourType>, List<List<E>>, List<Map<K,V>> or Map<K,V>.
    // E,K,V must be a scalar (String, Integer, Date, enum etc)
    DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
    Document parseTest = f.newDocumentBuilder()
        .parse(new InputSource(new StringReader(responseString)));
    for(String key : arg1.keySet()) {
      String xpath = key;
      String value = arg1.get(xpath);

      XPathFactory xPathFactory = XPathFactory.newInstance();
      XPath xPath = xPathFactory.newXPath();
      String fullXpath = sectionXpath + xpath;
      XPathExpression compile = xPath.compile(fullXpath);
      String evaluate = compile.evaluate(parseTest);
      assertEquals(xpath, value, evaluate);
    }
  }
}