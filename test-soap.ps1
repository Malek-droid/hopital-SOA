$soapRequest = @"
<?xml version="1.0" encoding="UTF-8"?>
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="http://hospital.com/rendezvous">
    <soap:Header>
        <Authorization xmlns="http://hospital.com/security">Bearer dummy-token-for-testing</Authorization>
    </soap:Header>
    <soap:Body>
        <urn:testConnection/>
    </soap:Body>
</soap:Envelope>
"@

$headers = @{
    'Content-Type' = 'text/xml; charset=utf-8'
    'SOAPAction'   = ''
}

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8084/services/RendezvousService" -Method POST -Headers $headers -Body $soapRequest -UseBasicParsing
    
    Write-Host "Success! Status Code: $($response.StatusCode)"
    Write-Host "Response:"
    Write-Host $response.Content
}
catch {
    Write-Host "Error! Status Code: $($_.Exception.Response.StatusCode.Value__)"
    Write-Host "Error Message: $($_.Exception.Message)"
    
    if ($_.Exception.Response) {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        Write-Host "Response Body:"
        Write-Host $responseBody
    }
}
