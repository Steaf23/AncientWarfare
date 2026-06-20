extends Node


func _ready() -> void:
	var file: FileAccess = FileAccess.open("res://input/faction_npc_defaults.json", FileAccess.READ)
	var input = JSON.parse_string(file.get_as_text())
	file.close()
	
	var enabled_types = []
	for type in input.defaults.npc_subtypes.keys():
		var sub_type = input.defaults.npc_subtypes[type]
		if (!"enabled" in sub_type) or sub_type.enabled:
			enabled_types.append(type)
	
	
	var output = ""
	for faction_id in input.factions.keys():
		var faction = input.factions[faction_id]
		output += "var " + faction_id.to_camel_case() + " = factionBuilder(\"" + faction_id + "\")\n"
		
		for arg in faction.keys():
			var value = faction[arg]
			if arg == "npc_subtypes":
				output = output.substr(0, output.length()  -1) + ";\n"
				var all_types = faction.npc_subtypes.keys()
				all_types.append_array(enabled_types)
		
				for existing_type: String in all_types:
					var npc = {}
					var default = false
					if existing_type in faction.npc_subtypes:
						npc = faction.npc_subtypes[existing_type]
					else:
						npc = input.defaults.npc_subtypes[existing_type]
						default = true

					var npc_type = existing_type.split(".")
					if "spellcaster" not in npc_type:
						npc_type.reverse()
						
					npc_type = "_".join(npc_type)
					output += convert_npc(default, npc_type, faction_id, npc)
			else:
				output += parse_attribute(arg, value)
		
		output += "\n"
	file = FileAccess.open("res://output/output.txt", FileAccess.WRITE_READ)
	file.store_string(output)
	file.close()


func convert_npc(default: bool, id: String, faction: String, npc: Dictionary) -> String:
	if not npc.get("enabled", true):
		return ""

	var res = "saveNpc(build"
	if "spellcaster" in id:
		res += "Spellcaster(" + faction.to_camel_case() + ", " 
		if "_" in id:
			res += id.replace("spellcaster_", "") + ")\n"
		else:
			res += "0)\n"
	else:
		res += id.to_pascal_case() + "(" + faction.to_camel_case() + ")\n"
	
	if !default:
		for key in npc.keys():
			var value = npc[key]
			res += parse_attribute(key, value)
	res = res.substr(0, res.length()  -1) + ");\n"
	return res
	

func add_equipment(value: Dictionary) -> String:
	var res = ""
	if value.get("mainhand") == "minecraft:air":
		return res
		
	res += "\t\t."
	if "mainhand" in value or "offhand" in value:
		res += "equipment("
		if "mainhand" in value:
			res += "Identifier.parse(\"" + value.mainhand + "\")"
		else:
			res += "null"
		res += ", "
		if "offhand" in value:
			res += "Identifier.parse(\"" + value.offhand + "\")"
		else:
			res += "null"
		
		res += ")\n"
	return res


func add_attribute(attribute: String, value: int) -> String:
	return "\t\t.addAttribute(Attributes." + attribute + ", " + str(value) + ")\n"


func add_additional_attributes(value: Dictionary) -> String:
	var res = ""
	for a in value.keys():
		var a_value = value[a]
		res += "\t\t."
		
		match a:
			"burns_in_sun": res += "burnsInSun(" + str(a_value)
			"horse_entity": res += "simpleMount(EntityType." + a_value.split(".")[-1].replace("Entity", "").to_snake_case().to_upper()
			"entity_sound": res += "soundSet(AncientWarfare.id(\"" + a_value + "\")"
			"undead": res += "undead(" + str(a_value)
			"can_swim": res += "canSwim(" + str(a_value)
		res += ")\n"
	
	return res
		
func parse_attribute(key: String, value: Variant) -> String:
	var res = ""
	match key:
		"equipment":
			res += add_equipment(value)
		"attributes":
			for a in value.keys():
				res += add_attribute(a.replace("generic.", "").to_snake_case().to_upper(), value[a])
		"experience_drop":
			res += "\t\t.experienceDropped(" + str(int(value)) + ")\n"
		"additional_attributes":
			res += add_additional_attributes(value)
		"spells":
			var existing = []
			var spells: PackedStringArray = []
			for s in value.split(","):
				if not s.is_empty() and s not in existing:
					existing.append(s)
					spells.append("Identifier.parse(\"" + s + "\")")
			res += "\t\t.spells(Set.of(" + ", ".join(spells) + "))\n";
	return res
