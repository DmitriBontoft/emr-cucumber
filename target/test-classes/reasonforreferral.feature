Feature:
  Scenario: Validate the conformance
    Given User is logged in
    When User requests a Visit Summary for patient 9198 encounter 25529
    Then the section at //section[code/@code='42349-1'] will contain the following values
      | /templateId[1]/@root        | 1.3.6.1.4.1.19376.1.5.3.1.3.1   |
      | /templateId[2]/@root        | 1.3.6.1.4.1.19376.1.5.3.1.3.1   |
      | /templateId[2]/@extension   | 2014-06-09                      |
      | /templateId[1]/@root	    | 1.3.6.1.4.1.19376.1.5.3.1.3.1   |
      | /templateId[2]/@root        |	1.3.6.1.4.1.19376.1.5.3.1.3.1 |
      | /templateId[2]/@extension	| 2014-06-09                      |
      | /id/@root                   |	2.16.840.1.113883.4.391       |
      | /id/@extension	            | 3                               |
      | /code/@code	                | 42349-1                         |
      | /code/@codeSystem	        | 2.16.840.1.113883.6.1           |
      | /code/@codeSystemName	    | LOINC                           |

  Scenario: Validate the content
    Given User is logged in
    When User requests a Visit Summary for patient 9198 encounter 25529
    Then the section at ClinicalDocument/component/structuredBody/component/section[code/@code='42349-1']/text/table/tbody/tr will contain the following values
      | /th[contains(text(), "Reason")]/following-sibling::td/text()                        | P@t!ent is not well . Need$ urgent treatment . !@#$%^&*()(*&^%$#@!?><:""{}\'" |
      | /th[contains(text(), "Diagnosis")]/following-sibling::td/text()                     | Abdominal actinomycosis                                                       |
      | /th[contains(text(), "Referral Organization")]/following-sibling::td/text()         | Test Facility                                                                 |
      | /th[contains(text(), "Referring Provider First Name")]/following-sibling::td/text() | Sam                                                                           |
      | /th[contains(text(), "Referring Provider Last Name")]/following-sibling::td/text()  | Willis                                                                        |
      | /th[contains(text(), "Referring Provider Specialty")]/following-sibling::td/text()  |                                                                               |
      | /th[contains(text(), "Referring Provider Phone")]/following-sibling::td/text()      |                                                                               |
      | /th[contains(text(), "Referring Provider email")]/following-sibling::td/text()      |                                                                               |
      | /th[contains(text(), "Referred Organization")]/following-sibling::td/text()         | Test Facility                                                                 |
      | /th[contains(text(), "Referred Provider")]/following-sibling::td/text()             | Cena,John                                                                     |
      | /th[contains(text(), "Referred Address")]/following-sibling::td/text()              | 2 TECHNOLOGY DRIVE,WEST BOROUGH,MA,01583                                      |
      | /th[contains(text(), "Referred Provider Specialty")]/following-sibling::td/text()   | Anesthesiology                                                                |
      | /th[contains(text(), "Referral Priority")]/following-sibling::td/text()             | Routine                                                                       |

  Scenario: Validate No Information
    Given User is logged in
    When User requests a Visit Summary for patient 9134 encounter 23974
    Then the section at ClinicalDocument/component/structuredBody/component/section[code/@code='42349-1'] will contain the following values
      | /templateId[1]/@root          | 1.3.6.1.4.1.19376.1.5.3.1.3.1 |
      | /templateId[2]/@root          | 1.3.6.1.4.1.19376.1.5.3.1.3.1 |
      | /templateId[2]/@extension     | 2014-06-09                    |
      | /code/@code                   | 42349-1                       |
      | /code/@codeSystem             | 2.16.840.1.113883.6.1         |
      | /code/@codeSystemName         | LOINC                         |
      | /code/@displayName            | REASON FOR REFERRAL           |
      | /text                         | No Information                |