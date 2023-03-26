# Bayes Java Dota Challlenge

End Points
1) An endpoint to receive a combat log text file and ingest it into the system

http://localhost:8080/api/match

![image](https://user-images.githubusercontent.com/128981420/227789826-b7e33388-093d-4be9-849e-b7d41a99a575.png)

2) Endpoint to get  list of the heroes within a match and the number of kills they made

http://localhost:8080/api/match/1

![image](https://user-images.githubusercontent.com/128981420/227790121-ef83a564-d1f2-467a-8522-3c37a83b4bfb.png)

![image](https://user-images.githubusercontent.com/128981420/227790159-f8d4d952-c54a-4844-ad58-14e41cd54f3a.png)

3) Endpoint to get items brought with timestamp by hero for a given match

http://localhost:8080/api/match/2/mars/items

![image](https://user-images.githubusercontent.com/128981420/227790682-755a0a43-8a46-4ebc-b4e4-b9f5d49e72e6.png)

![image](https://user-images.githubusercontent.com/128981420/227790711-aa27d4d4-8e4e-4475-9d15-bc7ae30fa981.png)


4) Endpoint to  get different spell a hero casts in a particular match, the number of times they cast said spell

http://localhost:8080/api/match/2/mars/spells

![image](https://user-images.githubusercontent.com/128981420/227791109-8f550c8f-423e-4a0d-b08d-a2957e15980f.png)

![image](https://user-images.githubusercontent.com/128981420/227791145-0499ac7e-3340-460e-ac5f-482664f5e15f.png)


5) Endpoint to get for each hero that was damaged by the specified hero, the number of times we damaged that hero, and the total damage done to that hero

[http://localhost:8080/api/match/1/rubick/damage

![image](https://user-images.githubusercontent.com/128981420/227791436-f125cbe9-9c2a-439b-8271-4af24cbed9fc.png)

![image](https://user-images.githubusercontent.com/128981420/227791462-ca72fba4-1a01-4cef-8eb6-1e44797b42ec.png)







