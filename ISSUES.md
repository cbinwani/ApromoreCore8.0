# Issues
This document is the development to-do list.
It should eventually be replaced by a proper bug tracker.

# Defects
- The ui component's ZK integration needs improvement.  Specifically:
  - The ZK user session manager intermittently fails to start, which cascades into preventing the asynchronous update servlet, which jams the entire UI.
  - Widgets such as the BPMN editor should reside in their own plugins rather than ui.
- Menu configuration needs improvement.  Specifically:
  - The `ui.menuOrder` property should be internationalized.  The default value is an English ordering; other locales revert to alphabetical order.
  - Menu item order cannot be configured, and is always alphabetical.
- The login dialog should be marked up as a form so that browser auto-login works.
- The persistence layer's subclassing of Items requires XA transactions -- the overhead of this needs to be quantified and justified.
- The raffaeleconforti-osgi bundle needs to be factored into sublibraries and have unlicensed code expunged.
- Exceptions from the persistence layer cause UndeclaredThrowableException (this might be due purely to JPA automatic table creation, which should only be for development anyway).
- The URL / should redirect to /index.zul.
- Warning during build: `Unable to determine whether the meta annotation javax.enterprise.util.Nonbinding applied to type org.apromore.bpmn_item.impl.BPMNItemPluginImpl provides bundle annotations as it is not on the project build path. If this annotation does provide bundle annotations then it must be present on the build path in order to be processed`

## Features
- Job management: submitting Runnables for off-thread execution, with a UI to monitor progress and allow cancellation
- Introduce a lower-level MVC architecture for actions/attributes (business objects) possible within Apromore, e.g. `delete(session.selection)` is an action bound to a menu item, `session.user` is a value bound to a login indicator.
- The log service could provide a directly-follows graph representation.  XES events are the vertices of a graph; a DFG would be the edges of the graph, and those edges have their own anotations we'd like to memoize.
- Configuration for additional databases: Derby, SQLite
- ZK window management
  - Opening ZK windows in separate browser windows/tabs
  - Vertical resizing of windows
  - Vertical scrolling rather than paging (render on demand?)
  - Horizontal panning
- [Custom ZK theme](https://www.zkoss.org/wiki/Small_Talks/2016/May/New_Approach_for_Building_Custom_ZK_Theme) with a proper dark mode
- Logging needs to have a clear policy so it's not just a blizzard of TL;DR
  - Logs for system administators, particularly in the case of clusters
  - Logs for developers
