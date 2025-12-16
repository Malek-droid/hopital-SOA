$headers = @{
    "Content-Type" = "text/xml; charset=utf-8"
    "SOAPAction"   = ""
}

$body = @"
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tns="http://hospital.com/rendezvous">
    <soap:Header>
        <Authorization xmlns="http://hospital.com/rendezvous">Bearer test-token</Authorization>
    </soap:Header>
    <soap:Body>
        <tns:testConnection/>
    </soap:Body>
</soap:Envelope>
"@

Write-Host "Testing Direct Access (Port 8084)..."
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8084/services/RendezvousService" -Method Post -Headers $headers -Body $body -ErrorAction Stop
    Write-Host "Direct Access Status: $($response.StatusCode)"
    Write-Host "Response: $($response.Content)"
}
catch {
    Write-Host "Direct Access Failed: $_"
    if ($_.Exception.Response) {
        Write-Host "Status: $($_.Exception.Response.StatusCode)"
    }
}

Write-Host "`nTesting Gateway Access (Port 8082)..."
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8082/services/RendezvousService" -Method Post -Headers $headers -Body $body -ErrorAction Stop
    Write-Host "Gateway Access Status: $($response.StatusCode)"
    Write-Host "Response: $($response.Content)"
}
catch {
    Write-Host "Gateway Access Failed: $_"
    if ($_.Exception.Response) {
        Write-Host "Status: $($_.Exception.Response.StatusCode)"
    }
}
