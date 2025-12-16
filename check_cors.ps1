$url = "http://localhost:8082/services/RendezvousService"
$origin = "http://localhost:8083"

$soap = @"
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/" xmlns:tns="http://hospital.com/rendezvous">
   <soap:Header>
       <Authorization xmlns="http://hospital.com/rendezvous">Bearer valid_token</Authorization>
   </soap:Header>
    <soap:Body>
        <tns:testConnection/>
    </soap:Body>
</soap:Envelope>
"@

try {
    $request = [System.Net.HttpWebRequest]::Create($url)
    $request.Method = "POST"
    $request.ContentType = "text/xml"
    $request.Headers.Add("Origin", $origin)
    
    $bytes = [System.Text.Encoding]::UTF8.GetBytes($soap)
    $request.ContentLength = $bytes.Length
    $stream = $request.GetRequestStream()
    $stream.Write($bytes, 0, $bytes.Length)
    $stream.Close()

    $response = $request.GetResponse()
    
    Write-Host "Response Status: $($response.StatusCode)"
    Write-Host "--- Headers ---"
    foreach ($key in $response.Headers.AllKeys) {
        Write-Host "$key : $($response.Headers[$key])"
    }
    
    $reader = New-Object System.IO.StreamReader($response.GetResponseStream())
    $content = $reader.ReadToEnd()
    # Write-Host "Content: $content"
}
catch {
    Write-Host "Error: $_"
    if ($_.Exception.Response) {
        $resp = $_.Exception.Response
        Write-Host "Status: $($resp.StatusCode)"
        Write-Host "--- Headers ---"
        foreach ($key in $resp.Headers.AllKeys) {
            Write-Host "$key : $($resp.Headers[$key])"
        }
    }
}
