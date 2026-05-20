folder = "src/main/resources/assets/ancientwarfare/template/"


import os

def write_filepaths(f, cur_folder) -> None:
	for filePath in os.listdir(cur_folder):
		fullPath = cur_folder + filePath
		if os.path.isfile(fullPath) and fullPath.endswith(".aws"):
			f.write(fullPath.replace(folder, "") + "\n")
		elif os.path.isdir(fullPath):
			write_filepaths(f, fullPath + "/")

with open(folder + "all_structures.txt", "w") as f:
	write_filepaths(f, folder)