package nuparu.sevendaystomine.computer.process.epidemic;

import java.util.ArrayList;


public class EpidemicHelper {

	public static void initCountries(EpidemicProcess process) {
		process.countries.clear();
		EpidemicCountry canada = new EpidemicCountry("canada", process);
		canada.population = 38718254;
		canada.airports = 13;
		canada.ports = 15;
		// Since most of Canadians live close to the US border, we will just pretend the
		// country is a bit smaller (original are 11703)
		canada.area = 7500;
		process.countries.add(canada);

		EpidemicCountry usa = new EpidemicCountry("usa", process);
		usa.population = 327529274;
		usa.infected = 0;
		usa.airports = 16;
		usa.ports = 10;
		usa.area = 8116;
		process.countries.add(usa);

		EpidemicCountry mexico = new EpidemicCountry("mexico", process);
		mexico.population = 128649550;
		mexico.airports = 5;
		mexico.ports = 4;
		mexico.area = 1973;
		process.countries.add(mexico);

		EpidemicCountry central_america = new EpidemicCountry("central_america", process);
		central_america.population = 47448336;
		central_america.airports = 2;
		central_america.ports = 5;
		central_america.area = 521;
		process.countries.add(central_america);

		EpidemicCountry colombia = new EpidemicCountry("colombia", process);
		colombia.population = 50372424;
		colombia.airports = 2;
		colombia.ports = 3;
		colombia.area = 1143;
		process.countries.add(colombia);

		EpidemicCountry venezuela = new EpidemicCountry("venezuela", process);
		venezuela.population = 28887118;
		venezuela.airports = 2;
		venezuela.ports = 3;
		venezuela.area = 916;
		process.countries.add(venezuela);

		EpidemicCountry brazil = new EpidemicCountry("brazil", process);
		brazil.population = 213665677;
		brazil.airports = 2;
		brazil.ports = 7;
		brazil.area = 8752;
		process.countries.add(brazil);

		EpidemicCountry guyana = new EpidemicCountry("guyana", process);
		guyana.population = 1653072;
		guyana.airports = 2;
		guyana.ports = 4;
		guyana.area = 460;
		process.countries.add(guyana);

		EpidemicCountry bolivia = new EpidemicCountry("bolivia", process);
		bolivia.population = 11428245;
		bolivia.airports = 2;
		bolivia.ports = 0;
		bolivia.area = 1099;
		process.countries.add(bolivia);

		EpidemicCountry peru = new EpidemicCountry("peru", process);
		peru.population = 49872716;
		peru.airports = 2;
		peru.ports = 3;
		peru.area = 1568;
		process.countries.add(peru);

		EpidemicCountry greenland = new EpidemicCountry("greenland", process);
		greenland.population = 56081;
		greenland.airports = 1;
		greenland.ports = 2;
		// 2166
		greenland.area = 10;
		process.countries.add(greenland);

		EpidemicCountry patagonia = new EpidemicCountry("patagonia", process);
		patagonia.population = 69805387;
		patagonia.airports = 2;
		patagonia.ports = 4;
		patagonia.area = 3944;
		process.countries.add(patagonia);

		EpidemicCountry caribbean = new EpidemicCountry("caribbean", process);
		caribbean.population = 41510745;
		caribbean.airports = 1;
		caribbean.ports = 8;
		caribbean.area = 300;
		process.countries.add(caribbean);

		EpidemicCountry iceland = new EpidemicCountry("iceland", process);
		iceland.population = 364134;
		iceland.airports = 1;
		iceland.ports = 1;
		iceland.area = 103;
		process.countries.add(iceland);

		EpidemicCountry british_isles = new EpidemicCountry("british_isles", process);
		british_isles.population = 74458732;
		british_isles.airports = 6;
		british_isles.ports = 12;
		british_isles.area = 326;
		process.countries.add(british_isles);

		EpidemicCountry iberia = new EpidemicCountry("iberia", process);
		iberia.population = 57727165;
		iberia.airports = 4;
		iberia.ports = 4;
		iberia.area = 597;
		process.countries.add(iberia);

		EpidemicCountry western_europe = new EpidemicCountry("western_europe", process);
		western_europe.population = 203238488;
		western_europe.airports = 20;
		western_europe.ports = 18;
		western_europe.area = 1242;
		process.countries.add(western_europe);

		EpidemicCountry italy = new EpidemicCountry("italy", process);
		italy.population = 60905037;
		italy.airports = 8;
		italy.ports = 8;
		italy.area = 301;
		process.countries.add(italy);

		EpidemicCountry central_europe = new EpidemicCountry("central_europe", process);
		central_europe.population = 64304391;
		central_europe.airports = 8;
		central_europe.ports = 4;
		central_europe.area = 532;
		process.countries.add(central_europe);

		EpidemicCountry balkans = new EpidemicCountry("balkans", process);
		balkans.population = 41476418;
		balkans.airports = 4;
		balkans.ports = 8;
		balkans.area = 500;
		process.countries.add(balkans);

		EpidemicCountry eastern_europe = new EpidemicCountry("eastern_europe", process);
		eastern_europe.population = 75354149;
		eastern_europe.airports = 7;
		eastern_europe.ports = 6;
		eastern_europe.area = 1081;
		process.countries.add(eastern_europe);

		EpidemicCountry australia = new EpidemicCountry("australia", process);
		australia.population = 25718700;
		australia.airports = 3;
		australia.ports = 5;
		australia.area = 7692;
		process.countries.add(australia);

		EpidemicCountry new_zealand = new EpidemicCountry("new_zealand", process);
		new_zealand.population = 5108220;
		new_zealand.airports = 3;
		new_zealand.ports = 5;
		new_zealand.area = 268;
		process.countries.add(new_zealand);

		EpidemicCountry russia = new EpidemicCountry("russia", process);
		russia.population = 143392231;
		russia.airports = 8;
		russia.ports = 8;
		russia.area = 17083;
		process.countries.add(russia);

		EpidemicCountry scandinavia = new EpidemicCountry("scandinavia", process);
		scandinavia.population = 21263549;
		scandinavia.airports = 2;
		scandinavia.ports = 12;
		scandinavia.area = 1173;
		process.countries.add(scandinavia);

		EpidemicCountry china = new EpidemicCountry("china", process);
		china.population = 1400050000;
		china.airports = 6;
		china.ports = 18;
		china.area = 8596;
		process.countries.add(china);

		EpidemicCountry indonesia = new EpidemicCountry("indonesia", process);
		indonesia.population = 276605543;
		indonesia.airports = 0;
		indonesia.ports = 6;
		indonesia.area = 2566;
		process.countries.add(indonesia);

		EpidemicCountry indochina = new EpidemicCountry("indochina", process);
		indochina.population = 277196068;
		indochina.airports = 0;
		indochina.ports = 8;
		indochina.area = 2070;
		process.countries.add(indochina);

		EpidemicCountry india = new EpidemicCountry("india", process);
		india.population = 1564672090;
		india.airports = 6;
		india.ports = 6;
		india.area = 3685;
		process.countries.add(india);

		EpidemicCountry pakistan = new EpidemicCountry("pakistan", process);
		pakistan.population = 212228286;
		pakistan.airports = 4;
		pakistan.ports = 3;
		pakistan.area = 881;
		process.countries.add(pakistan);

		EpidemicCountry afghanistan = new EpidemicCountry("afghanistan", process);
		afghanistan.population = 32225560;
		afghanistan.airports = 1;
		afghanistan.ports = 0;
		afghanistan.area = 652;
		process.countries.add(afghanistan);

		EpidemicCountry iran = new EpidemicCountry("iran", process);
		iran.population = 83183741;
		iran.airports = 2;
		iran.ports = 3;
		iran.area = 1648;
		process.countries.add(iran);

		EpidemicCountry baltic_states = new EpidemicCountry("baltic_states", process);
		baltic_states.population = 7026584;
		baltic_states.airports = 1;
		baltic_states.ports = 2;
		baltic_states.area = 189;
		process.countries.add(baltic_states);

		EpidemicCountry turkey = new EpidemicCountry("turkey", process);
		turkey.population = 83154997;
		turkey.airports = 2;
		turkey.ports = 6;
		turkey.area = 783;
		process.countries.add(turkey);

		EpidemicCountry caucasia = new EpidemicCountry("caucasia", process);
		caucasia.population = 16801632;
		caucasia.airports = 1;
		caucasia.ports = 1;
		caucasia.area = 164;
		process.countries.add(caucasia);

		EpidemicCountry mongolia = new EpidemicCountry("mongolia", process);
		mongolia.population = 3353470;
		mongolia.airports = 1;
		mongolia.ports = 0;
		mongolia.area = 1566;
		process.countries.add(mongolia);

		EpidemicCountry kazakhstan = new EpidemicCountry("kazakhstan", process);
		kazakhstan.population = 18711200;
		kazakhstan.airports = 1;
		kazakhstan.ports = 2;
		kazakhstan.area = 2724;
		process.countries.add(kazakhstan);

		EpidemicCountry central_asia = new EpidemicCountry("central_asia", process);
		central_asia.population = 55726041;
		central_asia.airports = 0;
		central_asia.ports = 1;
		central_asia.area = 1283;
		process.countries.add(central_asia);

		EpidemicCountry levant = new EpidemicCountry("levant", process);
		levant.population = 87301488;
		levant.airports = 2;
		levant.ports = 2;
		levant.area = 747;
		process.countries.add(levant);

		EpidemicCountry japan = new EpidemicCountry("japan", process);
		japan.population = 125960000;
		japan.airports = 8;
		japan.ports = 8;
		japan.area = 377;
		process.countries.add(japan);

		EpidemicCountry korea = new EpidemicCountry("korea", process);
		korea.population = 77258702;
		korea.airports = 3;
		korea.ports = 3;
		korea.area = 220;
		process.countries.add(korea);

		EpidemicCountry philippines = new EpidemicCountry("philippines", process);
		philippines.population = 106651394;
		philippines.airports = 0;
		philippines.ports = 5;
		philippines.area = 300;
		process.countries.add(philippines);

		EpidemicCountry arabia = new EpidemicCountry("arabia", process);
		arabia.population = 86221765;
		arabia.airports = 4;
		arabia.ports = 8;
		arabia.area = 3237;
		process.countries.add(arabia);

		EpidemicCountry egypt = new EpidemicCountry("egypt", process);
		egypt.population = 100075480;
		egypt.airports = 4;
		egypt.ports = 8;
		egypt.area = 1010;
		process.countries.add(egypt);

		EpidemicCountry libya = new EpidemicCountry("libya", process);
		libya.population = 6871287;
		libya.airports = 0;
		libya.ports = 2;
		libya.area = 1759;
		process.countries.add(libya);

		EpidemicCountry maghreb = new EpidemicCountry("maghreb", process);
		maghreb.population = 92720450;
		maghreb.airports = 1;
		maghreb.ports = 2;
		maghreb.area = 3254;
		process.countries.add(maghreb);

		EpidemicCountry mauritania = new EpidemicCountry("mauritania", process);
		mauritania.population = 43185053;
		mauritania.airports = 0;
		mauritania.ports = 2;
		mauritania.area = 2742;
		process.countries.add(mauritania);

		EpidemicCountry dr_congo = new EpidemicCountry("dr_congo", process);
		dr_congo.population = 89561404;
		dr_congo.airports = 0;
		dr_congo.ports = 2;
		dr_congo.area = 2345;
		process.countries.add(dr_congo);

		EpidemicCountry angola = new EpidemicCountry("angola", process);
		angola.population = 31127674;
		angola.airports = 0;
		angola.ports = 2;
		angola.area = 1246;
		process.countries.add(angola);

		EpidemicCountry namibia = new EpidemicCountry("namibia", process);
		namibia.population = 2746745;
		namibia.airports = 1;
		namibia.ports = 2;
		namibia.area = 825;
		process.countries.add(namibia);

		EpidemicCountry nigeria = new EpidemicCountry("nigeria", process);
		nigeria.population = 206630269;
		nigeria.airports = 0;
		nigeria.ports = 2;
		nigeria.area = 923;
		process.countries.add(nigeria);

		EpidemicCountry niger = new EpidemicCountry("niger", process);
		niger.population = 22442831;
		niger.airports = 1;
		niger.ports = 2;
		niger.area = 1267;
		process.countries.add(niger);

		EpidemicCountry sudan = new EpidemicCountry("sudan", process);
		sudan.population = 54370789;
		sudan.airports = 1;
		sudan.ports = 2;
		sudan.area = 2505;
		process.countries.add(sudan);

		EpidemicCountry madagascar = new EpidemicCountry("madagascar", process);
		madagascar.population = 26262313;
		madagascar.airports = 1;
		madagascar.ports = 4;
		madagascar.area = 587;
		process.countries.add(madagascar);

		EpidemicCountry horn_of_africa = new EpidemicCountry("horn_of_africa", process);
		horn_of_africa.population = 131222029;
		horn_of_africa.airports = 1;
		horn_of_africa.ports = 4;
		horn_of_africa.area = 1881;
		process.countries.add(horn_of_africa);

		EpidemicCountry south_africa = new EpidemicCountry("south_africa", process);
		south_africa.population = 65121027;
		south_africa.airports = 4;
		south_africa.ports = 5;
		south_africa.area = 1849;
		process.countries.add(south_africa);

		EpidemicCountry guinea = new EpidemicCountry("guinea", process);
		guinea.population = 126723945;
		guinea.airports = 4;
		guinea.ports = 5;
		guinea.area = 1468;
		process.countries.add(guinea);

		EpidemicCountry cameroon = new EpidemicCountry("cameroon", process);
		cameroon.population = 26545864;
		cameroon.airports = 4;
		cameroon.ports = 5;
		cameroon.area = 475;
		process.countries.add(cameroon);

		EpidemicCountry chad = new EpidemicCountry("chad", process);
		chad.population = 18336452;
		chad.airports = 4;
		chad.ports = 0;
		chad.area = 1906;
		process.countries.add(chad);

		EpidemicCountry east_africa = new EpidemicCountry("east_africa", process);
		east_africa.population = 170846988;
		east_africa.airports = 4;
		east_africa.ports = 0;
		east_africa.area = 1821;
		process.countries.add(east_africa);

		EpidemicCountry mozambique = new EpidemicCountry("mozambique", process);
		mozambique.population = 80764117;
		mozambique.airports = 0;
		mozambique.ports = 4;
		mozambique.area = 2061;
		process.countries.add(mozambique);

		EpidemicCountry gabon = new EpidemicCountry("gabon", process);
		gabon.population = 9029451;
		gabon.airports = 0;
		gabon.ports = 4;
		gabon.area = 638;
		process.countries.add(gabon);

		canada.addLandAdjacency(usa);
		usa.addLandAdjacency(mexico);
		mexico.addLandAdjacency(central_america);
		central_america.addLandAdjacency(colombia);
		colombia.addLandAdjacency(venezuela);
		colombia.addLandAdjacency(peru);
		colombia.addLandAdjacency(brazil);
		brazil.addLandAdjacency(venezuela);
		venezuela.addLandAdjacency(guyana);
		brazil.addLandAdjacency(guyana);
		brazil.addLandAdjacency(peru);
		brazil.addLandAdjacency(bolivia);
		brazil.addLandAdjacency(patagonia);
		peru.addLandAdjacency(bolivia);
		peru.addLandAdjacency(patagonia);
		bolivia.addLandAdjacency(patagonia);
		iberia.addLandAdjacency(western_europe);
		western_europe.addLandAdjacency(italy);
		western_europe.addLandAdjacency(central_europe);
		western_europe.addLandAdjacency(balkans);
		italy.addLandAdjacency(balkans);
		central_europe.addLandAdjacency(balkans);
		central_europe.addLandAdjacency(eastern_europe);
		central_europe.addLandAdjacency(baltic_states);
		eastern_europe.addLandAdjacency(baltic_states);
		balkans.addLandAdjacency(eastern_europe);
		eastern_europe.addLandAdjacency(russia);
		russia.addLandAdjacency(scandinavia);
		russia.addLandAdjacency(china);
		russia.addLandAdjacency(baltic_states);
		china.addLandAdjacency(indochina);
		china.addLandAdjacency(india);
		indochina.addLandAdjacency(india);
		china.addLandAdjacency(afghanistan);
		china.addLandAdjacency(pakistan);
		india.addLandAdjacency(pakistan);
		iran.addLandAdjacency(afghanistan);
		iran.addLandAdjacency(pakistan);
		iran.addLandAdjacency(turkey);
		balkans.addLandAdjacency(turkey);
		iran.addLandAdjacency(caucasia);
		turkey.addLandAdjacency(caucasia);
		russia.addLandAdjacency(caucasia);
		russia.addLandAdjacency(mongolia);
		china.addLandAdjacency(mongolia);
		kazakhstan.addLandAdjacency(mongolia);
		kazakhstan.addLandAdjacency(mongolia);
		kazakhstan.addLandAdjacency(russia);
		kazakhstan.addLandAdjacency(china);
		kazakhstan.addLandAdjacency(central_asia);
		china.addLandAdjacency(central_asia);
		afghanistan.addLandAdjacency(central_asia);
		iran.addLandAdjacency(central_asia);
		turkey.addLandAdjacency(levant);
		iran.addLandAdjacency(levant);
		china.addLandAdjacency(korea);
		russia.addLandAdjacency(korea);
		levant.addLandAdjacency(arabia);
		levant.addLandAdjacency(egypt);
		egypt.addLandAdjacency(libya);
		libya.addLandAdjacency(maghreb);
		maghreb.addLandAdjacency(mauritania);
		dr_congo.addLandAdjacency(angola);
		angola.addLandAdjacency(namibia);
		libya.addLandAdjacency(niger);
		maghreb.addLandAdjacency(niger);
		mauritania.addLandAdjacency(niger);
		niger.addLandAdjacency(nigeria);
		egypt.addLandAdjacency(sudan);
		dr_congo.addLandAdjacency(sudan);
		horn_of_africa.addLandAdjacency(sudan);
		namibia.addLandAdjacency(south_africa);
		mauritania.addLandAdjacency(guinea);
		nigeria.addLandAdjacency(guinea);
		niger.addLandAdjacency(guinea);
		nigeria.addLandAdjacency(cameroon);
		dr_congo.addLandAdjacency(cameroon);
		chad.addLandAdjacency(cameroon);
		chad.addLandAdjacency(libya);
		chad.addLandAdjacency(sudan);
		chad.addLandAdjacency(dr_congo);
		chad.addLandAdjacency(niger);
		chad.addLandAdjacency(nigeria);
		horn_of_africa.addLandAdjacency(east_africa);
		sudan.addLandAdjacency(east_africa);
		dr_congo.addLandAdjacency(east_africa);
		dr_congo.addLandAdjacency(mozambique);
		east_africa.addLandAdjacency(mozambique);
		angola.addLandAdjacency(mozambique);
		south_africa.addLandAdjacency(mozambique);
		namibia.addLandAdjacency(mozambique);
		dr_congo.addLandAdjacency(gabon);
		chad.addLandAdjacency(gabon);
		cameroon.addLandAdjacency(gabon);

		iceland.addNavalConnection(greenland);
		iceland.addNavalConnection(british_isles);
		usa.addNavalConnection(caribbean);
		usa.addNavalConnection(brazil);
		usa.addNavalConnection(british_isles);
		usa.addNavalConnection(iberia);
		usa.addNavalConnection(western_europe);
		usa.addNavalConnection(china);
		canada.addNavalConnection(greenland);
		canada.addNavalConnection(british_isles);
		patagonia.addNavalConnection(australia);
		patagonia.addNavalConnection(new_zealand);
		australia.addNavalConnection(new_zealand);
		scandinavia.addNavalConnection(western_europe);
		scandinavia.addNavalConnection(british_isles);
		scandinavia.addNavalConnection(iceland);
		scandinavia.addNavalConnection(baltic_states);
		australia.addNavalConnection(china);
		australia.addNavalConnection(indonesia);
		china.addNavalConnection(indonesia);
		china.addNavalConnection(indochina);
		indonesia.addNavalConnection(indochina);
		china.addNavalConnection(india);
		australia.addNavalConnection(india);
		turkey.addNavalConnection(italy);
		turkey.addNavalConnection(iberia);
		kazakhstan.addNavalConnection(caucasia);
		kazakhstan.addNavalConnection(central_asia);
		caucasia.addNavalConnection(central_asia);
		korea.addNavalConnection(japan);
		japan.addNavalConnection(china);
		japan.addNavalConnection(philippines);
		indonesia.addNavalConnection(philippines);
		usa.addNavalConnection(philippines);
		arabia.addNavalConnection(india);
		arabia.addNavalConnection(australia);
		egypt.addNavalConnection(iberia);
		egypt.addNavalConnection(india);
		libya.addNavalConnection(italy);
		india.addNavalConnection(madagascar);
		australia.addNavalConnection(madagascar);
		horn_of_africa.addNavalConnection(madagascar);
		horn_of_africa.addNavalConnection(arabia);
		horn_of_africa.addNavalConnection(india);
		south_africa.addNavalConnection(india);
		south_africa.addNavalConnection(madagascar);
		south_africa.addNavalConnection(australia);
		south_africa.addNavalConnection(patagonia);
		south_africa.addNavalConnection(mozambique);
		madagascar.addNavalConnection(mozambique);
		arabia.addNavalConnection(mozambique);

		for (EpidemicCountry other : new ArrayList<EpidemicCountry>(process.countries)) {
			if (other != usa && other.airports > 0) {
				usa.addAirConnection(other);
			}
		}

		australia.addAirConnection(new_zealand);
		australia.addAirConnection(china);
		british_isles.addAirConnection(russia);
		british_isles.addAirConnection(india);
		british_isles.addAirConnection(western_europe);
		british_isles.addAirConnection(turkey);
		australia.addAirConnection(india);
		china.addAirConnection(iran);
		china.addAirConnection(turkey);
		iran.addAirConnection(turkey);
		russia.addAirConnection(kazakhstan);
		russia.addAirConnection(caucasia);
		russia.addAirConnection(iran);
		russia.addAirConnection(russia);
		russia.addAirConnection(china);
		british_isles.addAirConnection(egypt);

	}
}
