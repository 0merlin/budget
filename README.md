# Simple Budget App

This app will allow you to record expenses from day to day.

It should work fine on most devices.

This app can use 3 tested currencies (ZAR, USD, GBP). I plan to add as many more that I can test.

*This app is created by a single person, so bugs will exist. I plan to make that as rare as possible. And I am using this app often, so I normally pickup bugs as I see them, and fix as soon as I can. So if you see any bugs, or would like some features create an issue or a pull request if you wish.*

## Current Features:

* Add expense quickly, with category
* Add new categories (but cannot remove, or re-arrange, they will always be alphabetical)
* Add notes on any expense
* View the expense from the 1st of this month
* View a summary of this month's expenses
* Export all data to JSON (which is almost a mirror of the internal DB)
* Backup to CSV for Excel
* Two widgets
  * Total expenditure of this month.
  * Summary
* The ability to use Google Now to add a expense:
  * `Ok Google, take a note spent <value without currency> on <category>`
  * An example: `Ok Google, take a note spent 50 on food`
* Editing your categories
* Manually enter expenses
* Specifying your currency
* Specifying a limit for the month
* Specyfying the start of your month
* Shows how much you have to spend for the day

## Permissions Required:

* `android.permission.WRITE_EXTERNAL_STORAGE` - Used to export the JSON file to primary external storage
* `android.permission.READ_EXTERNAL_STORAGE` - Not used yet, but might be used to import

## Planned future changes:

* Integration with Re-Hive <https://rehive.com> (will require internet permissions, and will be optional)
* View past month's data
* Compare previous months data to this month for trends
* Import of backup
* Backup to E-Mail

## Icons come from:

* designed by Madebyoliver from Flaticon <http://www.flaticon.com/packs/essential-set-2>
* designed by Iconnice from Flaticon <http://www.flaticon.com/packs/the-ultimate>
* designed by Freepik from Flaticon <http://www.flaticon.com/packs/electronic-and-web-element-collection--2>
* designed by Robin from Flaticon <http://www.flaticon.com/packs/circled-vol1 Kylander>
* designed by Freepik from Flaticon <http://www.flaticon.com/packs/lifestyle-icons >
* designed by Freepik from Flaticon <http://www.flaticon.com/packs/communication-and-media>
