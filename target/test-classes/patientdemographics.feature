Feature:
  Scenario:
    Given User is logged in
    When User requests a Visit Summary for patient 9198 encounter 25529
    Then The CCD element at "/ClinicalDocument/recordTarget" will contain the xml in "/xml/patientdemographics_full.xml"