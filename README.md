# Simple Budget App

This app will allow you to record expenses from day to day.

It should work fine on most devices.

Currently the app only uses ZAR as the currency, I will try make this more universal in time.

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

## Permissions Required:

* `android.permission.WRITE_EXTERNAL_STORAGE` - Used to export the JSON file to primary external storage
* `android.permission.READ_EXTERNAL_STORAGE` - Not used yet, but might be used to import

## Planned future changes:

* Integration with Re-Hive <https://rehive.com> (will require internet permissions, and will be optional)
* View past month's data
* Compare previous months data to this month for trends
* Import of backup
* Backup to E-Mail

## Some Screenshots:

Note that some of the screen are still in the process of being polished.

![Home Screen](main.png width=100)

![Value in Home screen](value.png width=100)

![Category Listing](category.png width=100)

![Menu on Home Screen](menu.png width=100)

![Summary Screen](summary.png width=100)

![Transaction Log](transaction_log.png width=100)


## Icons come from:

* designed by Madebyoliver from Flaticon <http://www.flaticon.com/packs/essential-set-2>
* designed by Iconnice from Flaticon <http://www.flaticon.com/packs/the-ultimate>