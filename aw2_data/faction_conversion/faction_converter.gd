extends Node

func _ready() -> void:
	var file: FileAccess = FileAccess.open("res://input/factions.json", FileAccess.READ)
	var input = JSON.parse_string(file.get_as_text())
	file.close()
	
	var output = ""
	for faction in input.factions:
		output += "saveFaction(new Faction(\n\t\tAncientWarfare.id(\"" + faction.name + "\"),\n"
		output += "\t\t0xff" + str(faction.color).to_lower() + ",\n"
		output += "\t\tSet.of(),\n"
		output += "\t\t" + str(int(faction.standing_settings.player_default_standing)) + ",\n"
		output += "\t\tMap.of(\n"
		output += "\t\t\t\tAncientWarfare.id(\"kill\"), " + str(int(faction.standing_settings.standing_changes.kill)) + ",\n"
		output += "\t\t\t\tAncientWarfare.id(\"trade\"), " + str(int(faction.standing_settings.standing_changes.trade)) + ")\n"
		
		output += "));\n"
		
	file = FileAccess.open("res://output/faction_output.txt", FileAccess.WRITE_READ)
	file.store_string(output)
	file.close()
