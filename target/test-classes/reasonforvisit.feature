Feature:
  Scenario: Reason for Visit CCD Section
    Given User is logged in
    When User requests a CCD for patient 9134 encounter 23974
    Then The CCD element at "/ClinicalDocument/component/structuredBody/component[3]" will contain the xml in "/xml/reasonforvisit.xml"

  Scenario: Reason for Visit CCD Section
    Given User is logged in
    When User requests a CCD for patient 9198 encounter 25529
    Then The CCD element at "/ClinicalDocument/component/structuredBody/component[3]" will contain the xml in "/xml/reasonforvisit_populated.xml"

