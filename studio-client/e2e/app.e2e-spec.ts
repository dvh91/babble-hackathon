import { BabbleClientPage } from './app.po';

describe('babble-client App', function() {
  let page: BabbleClientPage;

  beforeEach(() => {
    page = new BabbleClientPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
