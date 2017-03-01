import matplotlib.pyplot as plt
import sys

if (len(sys.argv) != 2):
    print("Greska, potrebno ime za izlaznu datoteku")
    sys.exit(-1)
    
xx = []
with open('izlaz-obj.txt', 'r') as f:
    xx = f.readlines()
    
x, y = [], []
for line in xx :
    tmp = line.split()
    x.append(float(tmp[0]))
    y.append(float(tmp[1]))

plt.xlim([0.1,1])
plt.ylim([0,10])
plt.scatter(x,y)
plt.savefig(sys.argv[1] + ".png")
