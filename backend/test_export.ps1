try {
    $r = Invoke-WebRequest -Uri 'http://localhost:8082/api/employees/2/export/pdf' -Method GET -UseBasicParsing
    Write-Host "Status: $($r.StatusCode)"
    Write-Host "ContentType: $($r.Headers['Content-Type'])"
    Write-Host "Content-Length: $($r.Content.Length)"
} catch {
    $resp = $_.Exception.Response
    Write-Host "Status: $($resp.StatusCode.value__)"
    $stream = $resp.GetResponseStream()
    $reader = New-Object System.IO.StreamReader($stream)
    $body = $reader.ReadToEnd()
    Write-Host "Error Body: $body"
}
