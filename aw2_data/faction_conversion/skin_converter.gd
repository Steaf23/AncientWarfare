extends Node

func _ready() -> void:
	var file: FileAccess = FileAccess.open("res://input/skin_pack.meta", FileAccess.READ)
	var input = file.get_as_text().split("\r\n")
	file.close()
	
	var order = []
	var types = {}
	var output = ""
	for line in input:
		var split = line.split("=")
		var id = split[0]
		var skin = split[1]
		if not id in types:
			types[id] = []
		types[id].append(skin)
		if not id in order:
			order.append(id)
	
	for id in order:
		var skins = types[id]
		var split = id.split(".", false, 1)
		if split.size() <= 1:
			continue
		
		var npc_name = split[1]
		if ".elite" in npc_name:
			npc_name = "elite_" + npc_name.replace(".elite", "")
		var id_converted = split[0] + "/" + npc_name.replace(".", "_")
		id_converted = id_converted.replace("spellcaster", "spell_caster")

		output += "generate(\"" + id_converted + "\", NpcModel.usingSkinPack("
		if skins.size() > 1:
			output += "List.of("
			for skin: String in skins:
				output += "\n\t\t\"" + skin.lstrip(" ").trim_suffix(".png") +"\","
			output = output.substr(0, output.length()  -1) + ")));\n"
		else:
			output += "\"" + skins[0].lstrip(" ").trim_suffix(".png") + "\"));\n"

		
	file = FileAccess.open("res://output/skin_output.txt", FileAccess.WRITE_READ)
	file.store_string(output)
	file.close()
