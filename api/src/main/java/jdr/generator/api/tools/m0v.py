import pyautogui
import random
import time

print("Width =", pyautogui.size()[0])
print("Height =", pyautogui.size()[1])

def click(x, y):
    """Simule un clic de souris à la position (x, y)."""
    pyautogui.click(x=x, y=y)

print("Take Rest!\nMouse pointer will keep moving!\n\nPress ctrl+c to stop...!")

while True:
    width, height = pyautogui.size()

    # Déplacements dans la moitié supérieure gauche de l'écran
    click(random.randrange(width // 2 - 250, width // 2),
          random.randrange(height // 2 - 250, height // 2))
    time.sleep(20)

    # Déplacements dans la moitié inférieure droite de l'écran
    click(random.randrange(width // 2 + 0, width // 2 + 250),
          random.randrange(height // 2 + 0, height // 2 + 250))
    time.sleep(20)