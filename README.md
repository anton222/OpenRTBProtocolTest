This is a simple size & speed test tool for various OpenRTB protocol implementations.
 
# Bulding and Running
 
 Requires maven and depends on https://github.com/google/openrtb
 
 Contains a test OpenRTB bid request in JSON format from OpenX, see http://docs.openx.com/ad_exchange_adv/index.html#openrtb_bidrequest_pmp_sample.html

 When running, supply a JSON file. The tool will transform it into a number of implementations and test parsing it back in a loop. 
 
# Results

    Sizes (bytes)
    JSON: 1339
    JSON.gz: 815
    Protobuf: 632
    Speed per 1000000 reads (ms)
    JSON: 13268
    JSON.gz: 34223
    Protobuf: 2761