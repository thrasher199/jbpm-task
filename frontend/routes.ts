
import { Route } from '@vaadin/router';
import Role from './generated/com/jbpmtask/application/data/entity/Role';

import { appStore } from './stores/app-store';
import './views/main-layout';
import './views/persondetail/person-detail-view';

export type ViewRoute = Route & {
  title?: string;
  icon?: string;
  requiresLogin?: boolean;
  rolesAllowed?: Role[];
  children?: ViewRoute[];
};

export const hasAccess = (route: Route) => {
  const viewRoute = route as ViewRoute;
  if (viewRoute.requiresLogin && !appStore.loggedIn) {
    return false;
  }

  if (viewRoute.rolesAllowed) {
    return viewRoute.rolesAllowed.some((role) => appStore.isUserInRole(role));
  }
  return true;
};

export const views: ViewRoute[] = [
  // place routes below (more info https://hilla.dev/docs/routing)
  {
    path: '',
    component: 'person-detail-view',
    requiresLogin: true,
    icon: '',
    title: '',
    action: async (_context, _command) => {
      if (!hasAccess(_context.route)) {
        return _command.redirect('login');
      }
      return;
    },
  },
  {
    path: 'person-detail',
    component: 'person-detail-view',
    requiresLogin: true,
    icon: 'la la-columns',
    title: 'Person Detail',
    action: async (_context, _command) => {
      if (!hasAccess(_context.route)) {
        return _command.redirect('login');
      }
      return;
    },
  },
  {
    path: 'user-detail',
    component: 'user-detail-view',
    icon: 'la la-user',
    title: 'User Detail',
    action: async (_context, _command) => {
      if (!hasAccess(_context.route)) {
        return _command.redirect('login');
      }
      await import('./views/userdetail/user-detail-view');
      return;
    },
  },
  {
    path: 'role-detail',
    component: 'role-detail-view',
    icon: 'la la-user',
    title: 'Role Detail',
    action: async (_context, _command) => {
      if (!hasAccess(_context.route)) {
        return _command.redirect('login');
      }
      await import('./views/roledetail/role-detail-view');
      return;
    },
  },
  {
    path: 'process-view',
    component: 'process-view',
    icon: 'la la-user',
    title: 'Process View',
    action: async (_context, _command) => {
      if (!hasAccess(_context.route)) {
        return _command.redirect('login');
      }
      await import('./views/process/process-view');
      return;
    },
  },
  {
    path: 'user-task-view',
    component: 'user-task-view',
    icon: 'la la-user',
    title: 'UserTask View',
    action: async (_context, _command) => {
      if (!hasAccess(_context.route)) {
        return _command.redirect('login');
      }
      await import('./views/task/user-task-view');
      return;
    },
  },
];
export const routes: ViewRoute[] = [
  {
    path: 'login',
    component: 'login-view',
    requiresLogin: true,
    icon: '',
    title: 'Login',
    action: async (_context, _command) => {
      await import('./views/login/login-view');
      return;
    },
  },

  {
    path: '',
    component: 'main-layout',
    children: [...views],
  },
];
