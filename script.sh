siege -c 10 -r 1000000000 \
  -H "Content-Type: application/json" \
  --content-type "application/json" \
  'http://localhost:8080/api/v1/payment POST {"description":"test","value":100.00}'