import sys
import os
import io
from prettytable import PrettyTable

def countLines(path, extension) :
	pt = PrettyTable()
	pt.field_names = ['filename', '#lines']
	total_line = 0
	total_files = 0
	for row in audit(path, extension, depth = 0) :
		if  row[0].endswith(extension) :
			pt.add_row([row[0], row[1]])
			total_line += row[1]
			total_files += 1
		else:
			pt.add_row([row[0], '-'])	

	pt.align['filename'] = 'l'
	print(pt.get_string())
	print('\n{} files found, in total {} lines'.format(total_files, total_line))

def audit(path, extension, depth):
	if not os.path.exists(path) :
		raise FileNotFoundError('invalid path')
	elif not os.path.isdir(path) :
		raise ValueError ('path does not specify a directory')
	else:

		os.chdir(path)
		all_    = os.listdir()
		files   = [name for name in all_ if name.endswith(extension)]
		folders = [name for name in all_ if os.path.isdir(os.path.join(path, name))]

		lst = list()
		for f_name in files :
			with open('./' + f_name, 'r') as f:
				num_lines = sum(1 for line in f)
				lst.append(['|  ' * depth + f_name, num_lines])

		for f_name in folders :
			lst.append(['+' + '--' * depth + f_name, '-'])
			next_path = os.path.join(path, f_name)
			lst.extend(audit(next_path, extension, depth + 1))

		return lst

# MAIN
if __name__ == "__main__":
	print("Enter folder path and filename extension on a line, separated by a whitespace: ")
		
	while (True) :
		try:	
			line = input('> ')
			parts = line.split()

			if len(parts) == 2:
				# path, extension
				countLines(parts[0], '.' + parts[1])
			else:
				print('must give a path and a filename extension')
		except KeyboardInterrupt:
			print('Abort. Send EOF to terminate')
			continue
		except EOFError:
			print('bye')
			break;
		except FileNotFoundError as fe:
			print(fe)
		except ValueError as ve:
			print(ve)
