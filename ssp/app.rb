require "rubygems"
require "sinatra"
require "pry"
require "json"

DSP_ENDPOINT1 = "http://127.0.0.1:9090/bidrequest"
DSP_ENDPOINT2 = "http://127.0.0.1:9090/bidrequest"
DSP_ENDPOINT3 = "http://127.0.0.1:9090/bidrequest"

def dsp_request(uri)
  req = Net::HTTP::Post.new(uri, initheader = {'Content-Type' =>'application/json'})
  req.body = {
    "id"=>"req-0",
    "imp"=>[{"id"=>"imp-0","bidfloor"=>0,"bidfloorcur"=>"JPY"}],
    "site"=>{"id"=>"GZCVlVEklesTBofbJXgXAqDZQ.jp","name"=>"VwTKwNYArwoi","page"=>"http=>//GZCVlVEklesTBofbJXgXAqDZQ.jp/umhFBGeiPHocWSMBdf"},
    "device"=>{"ua"=>"iOS","devicetype"=>1},
    "user"=>{"id"=>"user_1"},
    "test"=>1,"at"=>2,
    "tmax"=>1000
  }.to_json

  res = Net::HTTP.start(uri.hostname, uri.port) do |http|
    http.request(req)
  end

  res.body
end


get "/" do
  endpoints = [DSP_ENDPOINT1,
               DSP_ENDPOINT2,
               DSP_ENDPOINT3]

  #request dsp
  res = []
  endpoints.each do |ep|
    res << JSON.parse(dsp_request(URI.parse(ep)))["seatbid"].first["bid"]
  end

  #get highest bid

  #display from bid response
  "display ad from highest bid"
end
