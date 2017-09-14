import json

#########################################################################################################
# This file is a manager file that allocates on the computer, the file communicates with the firebase
# account, the code allows the user that work on the server to choose between the following methods:
# adding a new user to the system, deleting an existing user, printing all existing users, check if user
# is exist in the system or not, Remove ALL the existing users, adding details to an existing user.
# In order to facilitate the use at the server we create an interactive API that the user can choose the
# option that he wants.
#########################################################################################################

# #-------------------------------------------
import firebase_admin
from firebase_admin import credentials
from firebase_admin import auth

#
# Fetch the service account key JSON file contents
cred = credentials.Certificate('swimingpool-a3b12-firebase-adminsdk-wvxsp-6f3635eb88.json')
# the json key you download from the instructions

# Initialize the app with a service account, granting admin privileges
default_app = firebase_admin.initialize_app(cred, {'databaseURL': 'https://swimingpool-a3b12.firebaseio.com/'})
# As an admin, the app has access to read and write all data, regradless of Security Rules
# The app only has access as defined in the Security Rules
# #-------------------------------------------

#---------------------------------------------------------------------------------------
# Explanations of all the variables and functions
# The variables are repeated in all the functions and we will explain them once

# with -      we using in _with_ when we have two related operations that we want to
#             execute as a pair , with a block of code in between
# open() -    create a file object from our json file j.json , which would be utilized
#             to call other support methods associated with it
# json_data - new comfortable name for the json file j.json
# reading -   put here the data that read from the json file
# load() -    return an object from a string representing a json object
# user -      for each user that exist at reading we add him to the list of the users
#             user hold a Map that contains (key,value) from the json file
# append() -  add a value to an array , here we add string that contains the user name
#             and his password (secret with hash code)
# get() -     Returns the value of the associated (key) mapping for the specified name
# str() -     convert the type of the value to a string form
# hash() -    the hash code value for the password (works only on Integer)
#---------------------------------------------------------------------------------------

# check if User exist or not
def IsExist(name):
    with open('Users.json') as json_data:
        reading = json.load(json_data)

        for user in reading:
            if user.get("username") == name:
                print (name + " is Exist User")
                return 1

        print(name + " is UnExist User")
        return 0

    uid = auth.get_user_by_email(email=name).uid
    if (uid == None): print("there is no user")
    else: print("ExistUser")

#----------------------------------------------------------

def PrintUsers():

    print("\nUsers Names:")
    with open('Users.json') as json_data:
        reading = json.load(json_data)

        if len(reading) == 0:
            print ("No Users yet.")
            return 0

        for user in reading:
            print(user.get("username"))

    for user in auth._get_auth_service.user_manager:
        email = auth.get_user(uid=user).email
        print(email)
#----------------------------------------------------------

def AddUser(name, password):
    if IsExist(name) != 1:
        with open('Users.json') as json_data:
            reading = json.load(json_data)

            a_dict = {'username': name, 'password': hash(password)}
            reading.append(a_dict)

        with open('Users.json', 'w') as json_file:
            json.dump(reading, json_file)
        print(name, "is added")

        auth.create_user(email=name,password=password)
#----------------------------------------------------------

def AddAttribute(name,AttributeKey, AttributeValue):
    if IsExist(name) == 0:
        return "username " + name + " is UnExist"

    with open('Users.json') as json_data:
        reading = json.load(json_data)

        for user in reading:
            if user.get("username") == name:
                user[AttributeKey] = AttributeValue

    with open('Users.json', 'w') as json_file:
        json.dump(reading, json_file)

    print("the attribute", AttributeKey, "(",AttributeValue,")", "is added to", name)

    auth.update_user(name,AttributeKey,AttributeValue)
#----------------------------------------------------------

def RemoveAllUsers ():
    with open('Users.json') as json_data:
        reading = json.load(json_data)

        if len(reading) == 0:
            print ("No Users yet.")
            return 0

        for i in range(len(reading)-1):
            reading.pop(i)
        reading.pop(0)

    with open('Users.json', 'w') as json_file:
        json.dump(reading, json_file)

    print("All the Users are Removed")
    firebase_admin.delete_app('swimingpool-a3b12-firebase-adminsdk-wvxsp-6f3635eb88.json')
#----------------------------------------------------------

def RemoveUser (name, password):
    if IsExist(name) == 0:
        return "username " + name + " is UnExist"

    with open('Users.json') as json_data:
        reading = json.load(json_data)

        for i in range(len(reading)):
            if reading[i]["username"] == name:
                reading.pop(i)
                break

    with open('Users.json', 'w') as json_file:
        json.dump(reading, json_file)

    print(name, "is removed")

    uid2 = auth.get_user_by_email(email=name).uid
    auth.delete_user(uid=uid2)

#----------------------------------------------------------------------------------
import unittest

class TestSwimmingPool(unittest.TestCase):

    self = 'lidor@gmail.com'
    def Connect(self):
        AddUser(self)
        return IsExist(self)

    def RemoveAll(self):
        RemoveAllUsers()
        if (auth._get_auth_service().user_manager == None):
            return 1;
        else : return 0

if __name__ == '__main__':
    unittest.main()
#----------------------------------------------------------------------------------

#API
print()
print("Welcome to the Swimming Pool System")
print("You can select from the following functions - Please Select the required number\n")

print("1 - Add a new User")
print("2 - Remove an exist User")
print("3 - Present the list of Users that exist")
print("4 - Check if User is exist")
print("5 - Remove all the exist Users")
print("6 - Add Attribute to an exist User\n")

print("To Continue write C , To Exit write E")
chose = input('Continue or Exit ? ')

while (chose == 'C'):
    str = input('\nPlease enter a function Number: ')
    number = int(str)

    if(number == 1):
        print("\nYou chose to Add a new User")
        name = input('Please enter a Name: ')
        password = input('Please enter a password: ')
        AddUser(name,password)

    elif (number == 2):
        print("\nYou chose to Remove an exist User")
        name = input('Please enter a Name: ')
        password = input('Please enter a password: ')
        RemoveUser(name, password)

    elif (number == 3):
        print("\nYou chose to Present the list of Users that exist")
        PrintUsers()

    elif (number == 4):
        print("\nYou chose to Check if User is exist")
        name = input('Please enter a Name: ')
        IsExist(name)

    elif (number == 5):
        print("\nYou chose to Remove all the exist Users")
        RemoveAllUsers()

    else : # number = 6
        print("\nYou chose to Add Attribute to an exist User")
        name = input('Please enter a Name: ')
        Attribute = input('Please enter an Attribute: ')
        value = input('Please enter a value that the Attribute contains: ')
        AddAttribute(name,Attribute,value)

    print("\nTo Continue write C , To Exit write E")
    chose = input('Continue or Exit ? ')

print("\nYou chose to Exit")
print("Goodbye and thank you !")

#----------------------------------------------------------------------------------