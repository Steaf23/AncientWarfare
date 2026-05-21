import os

folder = "aw2_data/assets/ancientwarfare/template"


def write_filepaths(cur_folder) -> None:
	for filePath in os.listdir(cur_folder):
		fullPath = cur_folder + filePath
		if os.path.isfile(fullPath) and fullPath.endswith(".aws"):
			os.makedirs(cur_folder.replace("template", "template2"), exist_ok=True)
			replace_file(fullPath)
		
		elif os.path.isdir(fullPath):
			write_filepaths(fullPath + "/")


def replace_file(fullPath) -> None:
	data = ""
	bytes = []
	with open(fullPath, "r+") as f:
		data = f.read()
		print(fullPath, data.count("§"))
		data = data.replace("�", "§").replace("§", "%&%")
		bytes = data.encode("utf-8")
	with open(fullPath.replace("template", "template2"), "w") as f:
		f.write(bytes.decode("utf-8"))

write_filepaths(folder + "/")