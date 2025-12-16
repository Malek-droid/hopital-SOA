$url = "http://localhost:8082/services/RendezvousService"
$soap = @"
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tns="http://hospital.com/rendezvous">
    <soap:Body>
        <tns:testConnection/>
    </soap:Body>
</soap:Envelope>
"@

try {
    $response = Invoke-WebRequest -Uri $url -Method Post -Body $soap -ContentType "text/xml"
    Write-Host "Success: $($response.StatusCode)"
    Write-Host $response.Content
} catch {
    Write-Host "Error: $($_.Exception.Response.StatusCode.value__)"
    $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
    Write-Host $reader.ReadToEnd()
}
